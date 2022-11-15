/*
        Insecure Web App (IWA)

        Copyright (C) 2020-2022 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.repository;

import com.microfocus.example.entity.Product;
import com.microfocus.example.repository.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int count() {
        String sqlQuery = "select count(*) from products";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }

    public List<Product> findAll(int offset, int limit) {
        String sqlQuery = "select * from products" +
                " LIMIT " + limit + " OFFSET " + offset;
        return jdbcTemplate.query(sqlQuery, new ProductMapper());
    }

    public List<Product> findAvailable(int offset, int limit) {
        String sqlQuery = "select * from products" +
                " where available = true " +
                " LIMIT " + limit + " OFFSET " + offset;
        return jdbcTemplate.query(sqlQuery, new ProductMapper());
    }

    public Optional<Product> findById(UUID id) {
        List<Product> result = new ArrayList<>();
        String query = id.toString();
        String sqlQuery = "SELECT * FROM " + getTableName() +
                " WHERE id = '" + query + "'";
        result = jdbcTemplate.query(sqlQuery, new ProductMapper());
        Optional<Product> optionalProduct = Optional.empty();
        if (!result.isEmpty()) {
            optionalProduct = Optional.of(result.get(0));
        }
        return optionalProduct;
    }

    public boolean existsById(UUID id) {
        List<Product> result = new ArrayList<>();
        String query = id.toString().toLowerCase();
        String sqlQuery = "SELECT * FROM " + getTableName() +
                " WHERE id = '" + query + "'";
        result = jdbcTemplate.query(sqlQuery, new ProductMapper());
        if (result.isEmpty()) {
            return false;
        }
        return true;
    }

    public Optional<Product> findByCode(String code) {
        List<Product> result = new ArrayList<>();
        String query = code.toLowerCase();
        String sqlQuery = "SELECT * FROM " + getTableName() +
                " WHERE lower(code) = '" + query + "'";
        result = jdbcTemplate.query(sqlQuery, new ProductMapper());
        Optional<Product> optionalProduct = Optional.empty();
        if (!result.isEmpty()) {
            optionalProduct = Optional.of(result.get(0));
        }
        return optionalProduct;
    }

    public List<Product> findByKeywords(String keywords, int offset, int limit) {
        String query = keywords.toLowerCase();
        String sqlQuery = "SELECT * FROM " + getTableName() +
                " WHERE lower(name) LIKE '%" + query + "%' " +
                " OR lower(summary) LIKE '%" + query + "%'" +
                " OR lower(description) LIKE '%" + query + "%'"  +
                " LIMIT " + limit + " OFFSET " + offset;
        return jdbcTemplate.query(sqlQuery, new ProductMapper());
    }
    
    public List<Product> findByKeywordsFromProductName(String keywords) {
    	String query = keywords.toLowerCase();
    	String sqlQuery = "SELECT * FROM " + getTableName() + 
    			" WHERE lower(name) LIKE '%" + query + "%' ";
    	return jdbcTemplate.query(sqlQuery, new ProductMapper());
    }

    public List<Product> findAvailableByKeywords(String keywords, int offset, int limit) {
        String query = keywords.toLowerCase();
        String sqlQuery = "SELECT * FROM " + getTableName() +
                " WHERE lower(name) LIKE '%" + query + "%' " +
                " OR lower(summary) LIKE '%" + query + "%'" +
                " OR lower(description) LIKE '%" + query + "%'"  +
                " AND available = true " +
                " LIMIT " + limit + " OFFSET " + offset;
        return jdbcTemplate.query(sqlQuery, new ProductMapper());
    }

    public List<Product> findAvailableByKeywordsFromProductName(String keywords) {
    	String query = keywords.toLowerCase();
    	String sqlQuery = "SELECT * FROM " + getTableName() +
    			" WHERE available = true AND lower(name) LIKE '%" + query + "%' ";
    	return jdbcTemplate.query(sqlQuery, new ProductMapper());
    }
    
    public Product save(Product p) {
        UUID uuid = (p.getId() != null ? p.getId() : UUID.randomUUID());
        Product pRet = null;
        int status = 0;
        if (p.getId() != null && existsById(p.getId())) {
            log.debug("Updating existing product: " + p.getId());
            String sqlQuery = "UPDATE " + getTableName() + " SET" +
                    " code = ?, name = ?, rating = ?, summary = ?, description = ?, image = ?, price = ?," +
                    " on_sale = ?, sale_price = ?, in_stock = ?, time_to_stock = ?, available = ?" +
                    " WHERE id = ?";
            status = jdbcTemplate.update(sqlQuery, p.getCode(), p.getName(), p.getRating(), p.getSummary(),
                    p.getDescription(), p.getImage(), p.getPrice(), p.getOnSale(), p.getSalePrice(), p.getInStock(),
                    p.getTimeToStock(), p.getAvailable(),
                    p.getId());
        } else {
            log.debug("Creating new product");
            String sqlQuery = "INSERT INTO " + getTableName() +
                    " (id, code, name, rating, summary, description, image, price, on_sale, sale_price, in_stock, time_to_stock, available)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            status = jdbcTemplate.update(sqlQuery, uuid.toString(),
                    p.getCode(), p.getName(), p.getRating(), p.getSummary(), p.getDescription(), p.getImage(),
                    p.getPrice(), p.getOnSale(), p.getSalePrice(), p.getInStock(), p.getTimeToStock(), p.getAvailable());
        }
        if (status != 0) {
            Optional<Product> optionalProduct = findById(uuid);
            pRet = (optionalProduct.isPresent() ? optionalProduct.get() : null);
        }
        return pRet;
    }

    public void deleteById(UUID id) {
        if (existsById(id)) {
            log.debug("Deleting existing product: " + id.toString());
            String sqlQuery = "DELETE FROM " + getTableName() + " WHERE id = ?";
            int status = jdbcTemplate.update(sqlQuery, id);
            if (status != 0) {
                log.debug("Successfully deleted product");
            }
        }
    }

    String getTableName() {
        return Product.TABLE_NAME;
    }

}
