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
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @PersistenceContext
    EntityManager entityManager;

    public ProductRepositoryImpl(ProductRepositoryBasic productRepositoryBasic) {
        this.productRepositoryBasic = productRepositoryBasic;
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


// MORE SECURE EXAMPLE:

// Uncomment the following for more secure method:

    /*
    @SuppressWarnings("unchecked")
    public List<Product> findAvailableProductsByKeywords(String keywords, int offset, int limit) {
        List<Product> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.available = true AND lower(p.name) LIKE lower(?1)",
                Product.class);
        q.setParameter(1, "%"+keywords+"%");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Product>)q.getResultList();
        return result;
    }
    */

    @SuppressWarnings("unchecked")
    public List<Product> findAvailableProductsByKeywords(String keywords, int offset, int limit) {
        List<Product> products = new ArrayList<>();

       Session session = entityManager.unwrap(Session.class);
        Integer productCount = session.doReturningWork(new ReturningWork<Integer>() {

            @Override
            public Integer execute(Connection con) throws SQLException {
                Integer productCount = 0;
                try {
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    String query = "SELECT id, code, name, summary, description, image, price, " +
                            "on_sale, sale_price, in_stock, time_to_stock, rating, available FROM products WHERE lower(name) LIKE '%" +
                            keywords.toLowerCase() + "%' LIMIT " + limit + " OFFSET " + offset;
                    ResultSet results = stmt.executeQuery(query);
                    if (results.getStatement() != null) {
                        while (results.next()) {
                            productCount++;
                            products.add(new Product(results.getObject("id", UUID.class),
                                    results.getString("code"),
                                    results.getString("name"),
                                    results.getString("summary"),
                                    results.getString("description"),
                                    results.getString("image"),
                                    results.getFloat("price"),
                                    results.getBoolean("on_sale"),
                                    results.getFloat("sale_price"),
                                    results.getBoolean("in_stock"),
                                    results.getInt("time_to_stock"),
                                    results.getInt("rating"),
                                    results.getBoolean("available")
                            ));
                        }
                    } else {
                        log.debug("No products found");
                    }
                } catch (SQLException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                return productCount;
            }
        });
        log.debug("Found " + productCount + " products.");

        return products;
    }

}
