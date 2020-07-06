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
import com.microfocus.example.exception.ProductNotFoundException;
import com.microfocus.example.repository.ProductRepository;
import com.microfocus.example.web.form.admin.AdminNewProductForm;
import com.microfocus.example.web.form.admin.AdminProductForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Product Service to hide business logs / database persistence
 *
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
        return productRepository.findAll();
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

    public Product save(Product user) {
        return productRepository.save(user);
    }

    public Product get(Integer id) {
        return productRepository.findById(id).get();
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return productRepository.existsById(id);
    }

    public Product adminSave(AdminProductForm adminProductForm) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findByCode(adminProductForm.getCode());
        if (optionalProduct.isPresent()) {
            Product ptmp = optionalProduct.get();
            ptmp.setName(adminProductForm.getName());
            ptmp.setSummary(adminProductForm.getSummary());
            ptmp.setDescription(adminProductForm.getDescription());
            ptmp.setImage(adminProductForm.getImage());
            ptmp.setTradePrice(adminProductForm.getTradePrice());
            ptmp.setRetailPrice(adminProductForm.getRetailPrice());
            ptmp.setDeliveryTime(adminProductForm.getDeliveryTime());
            ptmp.setAverageRating(adminProductForm.getAverageRating());
            ptmp.setAvailable(adminProductForm.getAvailable());
            return ptmp;
        } else {
            throw new ProductNotFoundException("Product not found: " + adminProductForm.getCode());
        }
    }

    public Product adminAdd(AdminNewProductForm adminNewProductForm) {
        Product ptmp = new Product();
        ptmp.setCode(adminNewProductForm.getCode());
        ptmp.setName(adminNewProductForm.getName());
        ptmp.setSummary(adminNewProductForm.getSummary());
        ptmp.setDescription(adminNewProductForm.getDescription());
        ptmp.setTradePrice(adminNewProductForm.getTradePrice());
        ptmp.setRetailPrice(adminNewProductForm.getRetailPrice());
        ptmp.setDeliveryTime(adminNewProductForm.getDeliveryTime());
        ptmp.setImage(adminNewProductForm.getImage());
        ptmp.setAvailable(adminNewProductForm.getAvailable());
        Product newProduct = productRepository.saveAndFlush(ptmp);
        return newProduct;
    }

}
