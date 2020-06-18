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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
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

    @PersistenceContext
    EntityManager entityManager;

    public ProductRepositoryImpl(ProductRepositoryBasic productRepositoryBasic) {
        this.productRepositoryBasic = productRepositoryBasic;
    }

    @SuppressWarnings("unchecked")
    public List<Product> listProducts(int pageNumber, int pageSize) {
        List<Product> result = new ArrayList<>();
        int offset = (pageNumber == 1 ? 0 : ((pageNumber-1)*pageSize));
// INSECURE EXAMPLE: SQL Injection
        Query q = entityManager.createNativeQuery(
                "SELECT * FROM product LIMIT "  + pageSize + " OFFSET " + offset,
                Product.class);
// END EXAMPLE
        result = (List<Product>)q.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public Optional<Product> findByCode(String code) {
        org.hibernate.query.Query<Product> query = (org.hibernate.query.Query<Product>) entityManager.createNativeQuery("SELECT * FROM product WHERE code = ?", Product.class);
        query.setParameter(1, code);
        List<Product> results = (List<Product>) query.getResultList();
        Optional<Product> optionalProduct = Optional.empty();
        if (!query.getResultList().isEmpty()) {
            optionalProduct = Optional.of(results.get(0));
        }
        return optionalProduct;
    }

    @SuppressWarnings("unchecked")
    public List<Product> findProductsByKeywords(String keywords, int pageNumber, int pageSize) {
        List<Product> result = new ArrayList<>();
        int offset = (pageNumber == 1 ? 0 : ((pageNumber-1)*pageSize));
// INSECURE EXAMPLE: SQL Injection
        Query q = entityManager.createNativeQuery(
                "SELECT * FROM product WHERE name LIKE '%" + keywords + "%' LIMIT " + pageSize + " OFFSET " + offset,
                Product.class);
// END EXAMPLE

        result = (List<Product>)q.getResultList();
        return result;
    }
}
