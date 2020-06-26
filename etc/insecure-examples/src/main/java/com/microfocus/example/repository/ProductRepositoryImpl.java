/*
        Secure Web App

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
import javax.transaction.Transactional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of Custom Product Repository
 * @author Kevin A. Lee
 */
@Transactional
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    private final ProductRepositoryBasic productRepositoryBasic;

    public ProductRepositoryImpl(ProductRepositoryBasic productRepositoryBasic) {
        this.productRepositoryBasic = productRepositoryBasic;
    }

    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Product> listProducts(int pageNumber, int pageSize) {
        List<Product> products = new ArrayList<>();
        int offset = (pageNumber == 1 ? 0 : ((pageNumber-1)*pageSize));

// INSECURE EXAMPLE: SQL Injection

        Session session = entityManager.unwrap(Session.class);
        Integer productCount = session.doReturningWork(new ReturningWork<Integer>() {

            @Override
            public Integer execute(Connection con) throws SQLException {
                Integer productCount = 0;
                try {
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT id, code, name, summary, description, image, trade_price, " +
                            "retail_price, delivery_time, average_rating, available FROM product LIMIT " + pageSize +
                            " OFFSET " + offset);
                    if (results.getStatement() != null) {
                        while (results.next()) {
                            productCount++;
                            products.add(new Product(results.getInt("id"),
                                    results.getString("code"),
                                    results.getString("name"),
                                    results.getString("summary"),
                                    results.getString("description"),
                                    results.getString("image"),
                                    results.getFloat("trade_price"),
                                    results.getFloat("retail_price"),
                                    results.getInt("delivery_time"),
                                    results.getInt("average_rating"),
                                    results.getBoolean("available")
                            ));
                        }
                    } else {
                        log.info("No products found");
                    }
                } catch (SQLException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                return productCount;
            }
        });
        log.debug("Found " + productCount + " products.");

// END EXAMPLE

        return products;
    }

    @SuppressWarnings("unchecked")
    public Optional<Product> findByCode(String code) {
        List<Product> products = new ArrayList<>();
        Optional<Product> optionalProduct = Optional.empty();

// INSECURE EXAMPLE: SQL Injection

        Session session = entityManager.unwrap(Session.class);
        Integer productCount = session.doReturningWork(new ReturningWork<Integer>() {

            @Override
            public Integer execute(Connection con) throws SQLException {
                Integer productCount = 0;
                try {
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT id, code, name, summary, description, image, trade_price, " +
                            "retail_price, delivery_time, average_rating, available FROM product WHERE lower(code) = " +
                            code.toLowerCase());
                    if (results.getStatement() != null) {
                        while (results.next()) {
                            productCount++;
                            products.add(new Product(results.getInt("id"),
                                    results.getString("code"),
                                    results.getString("name"),
                                    results.getString("summary"),
                                    results.getString("description"),
                                    results.getString("image"),
                                    results.getFloat("trade_price"),
                                    results.getFloat("retail_price"),
                                    results.getInt("delivery_time"),
                                    results.getInt("average_rating"),
                                    results.getBoolean("available")
                            ));
                        }
                    } else {
                        log.info("No products found");
                    }
                } catch (SQLException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                return productCount;
            }
        });
        log.debug("Found " + productCount + " product(s).");

// END EXAMPLE

        if (!products.isEmpty()) {
            optionalProduct = Optional.of(products.get(0));
        }
        return optionalProduct;
    }

    @SuppressWarnings("unchecked")
    public List<Product> findProductsByKeywords(String keywords, int pageNumber, int pageSize) {
        List<Product> products = new ArrayList<>();
        int offset = (pageNumber == 1 ? 0 : ((pageNumber-1)*pageSize));

// INSECURE EXAMPLE: SQL Injection

        Session session = entityManager.unwrap(Session.class);
        Integer productCount = session.doReturningWork(new ReturningWork<Integer>() {

            @Override
            public Integer execute(Connection con) throws SQLException {
                Integer productCount = 0;
                try {
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT id, code, name, summary, description, image, trade_price, " +
                            "retail_price, delivery_time, average_rating, available FROM product WHERE lower(name) LIKE '%" +
                            keywords.toLowerCase() + "%' LIMIT " + pageSize + " OFFSET " + offset);
                    if (results.getStatement() != null) {
                        while (results.next()) {
                            productCount++;
                            products.add(new Product(results.getInt("id"),
                                    results.getString("code"),
                                    results.getString("name"),
                                    results.getString("summary"),
                                    results.getString("description"),
                                    results.getString("image"),
                                    results.getFloat("trade_price"),
                                    results.getFloat("retail_price"),
                                    results.getInt("delivery_time"),
                                    results.getInt("average_rating"),
                                    results.getBoolean("available")
                            ));
                        }
                    } else {
                        log.info("No products found");
                    }
                } catch (SQLException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                return productCount;
            }
        });
        log.debug("Found " + productCount + " products.");

// END EXAMPLE

        return products;
    }

}
