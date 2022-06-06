/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

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
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of Custom Product Repository
 * @author Kevin A. Lee
 */
@Transactional
public class ProductRepositoryImpl implements ProductRepositoryCustom {


    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    private final ProductRepositoryBasic productRepositoryBasic;
    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    EntityManager entityManager;

    public ProductRepositoryImpl(ProductRepositoryBasic productRepositoryBasic, JdbcTemplate jdbcTemplate) {
        this.productRepositoryBasic = productRepositoryBasic;
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("unchecked")
    public Optional<Product> findByCode(String code) {
        List<Product> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT p FROM Product p WHERE lower(p.code) = lower(?1)",
                Product.class);
        q.setParameter(1, code);
        result = (List<Product>)q.getResultList();
        Optional<Product> optionalProduct = Optional.empty();
        if (!result.isEmpty()) {
            optionalProduct = Optional.of(result.get(0));
        }
        return optionalProduct;
    }

    @SuppressWarnings("unchecked")
    public List<Product> listProducts(int offset, int limit) {
        List<Product> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT p FROM Product p",
                Product.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Product>)q.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Product> listAvailableProducts(int offset, int limit) {
        List<Product> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.available = true",
                Product.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Product>)q.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Product> findProductsByKeywords(String keywords, int offset, int limit) {
        List<Product> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT p FROM Product p WHERE lower(p.name) LIKE lower(?1)",
                Product.class);
        q.setParameter(1, "%"+keywords+"%");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Product>)q.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Product> findAvailableProductsByKeywords(String keywords, int offset, int limit) {
        String query = keywords.toLowerCase();
        String sqlQuery = "SELECT * FROM " + getTableName() +
                " WHERE lower(name) LIKE '%" + query + "%' " +
                " OR lower(summary) LIKE '%" + query + "%'" +
                " OR lower(description) LIKE '%" + query + "%'" +
                " LIMIT " + limit + " OFFSET " + offset;
        return jdbcTemplate.query(sqlQuery, new ProductMapper());
    }

    String getTableName() {
        return Product.TABLE_NAME;
    }

}
