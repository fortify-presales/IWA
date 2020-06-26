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

package com.microfocus.example.service;

import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.ProductNotFoundException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.repository.ProductRepository;
import com.microfocus.example.web.form.ProductForm;
import com.microfocus.example.web.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Product Service to hide business logs / database persistance
 * @author Kevin A. Lee
 */
@Service
@Transactional
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Value("${app.data.page-size:25}")
    private Integer pageSize;

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    public List<Product> listAll(Integer pageNo, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return productRepository.findProductsByKeywords(keywords, pageNo, pageSize);
        }
        return productRepository.listProducts(pageNo, pageSize);
    }

    public long count() {
        return productRepository.count();
    }

    public void save(Product user) {
        productRepository.save(user);
    }

    public Product adminSave(ProductForm productForm) throws ProductNotFoundException {
        log.debug("ProductService:adminSave: " + productForm.toString());
        Optional<Product> optionalProduct = productRepository.findByCode(productForm.getCode());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productForm.getName());
            product.setSummary(productForm.getSummary());
            product.setDescription(productForm.getDescription());
            product.setImage(productForm.getImage());
            product.setTradePrice(productForm.getTradePrice());
            product.setRetailPrice(productForm.getRetailPrice());
            product.setDeliveryTime(productForm.getDeliveryTime());
            product.setAverageRating(productForm.getAverageRating());
            product.setAvailable(productForm.getAvailable());
            return product;
        } else {
            throw new ProductNotFoundException("Product not found: " + productForm.getCode());
        }
    }

    public Product get(Integer id) {
        return productRepository.findById(id).get();
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

}
