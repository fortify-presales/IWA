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

package com.microfocus.example.service;

import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.Product;
import com.microfocus.example.exception.MessageNotFoundException;
import com.microfocus.example.exception.ProductNotFoundException;
import com.microfocus.example.payload.request.ProductRequest;
import com.microfocus.example.repository.OrderRepository;
import com.microfocus.example.repository.ProductRepository;
import com.microfocus.example.web.form.OrderForm;
import com.microfocus.example.web.form.admin.AdminNewProductForm;
import com.microfocus.example.web.form.admin.AdminProductForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private OrderRepository orderRepository;

    @Value("${app.data.page-size:25}")
    private Integer pageSize;

    public Optional<Product> findProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProducts(Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return productRepository.findProductsByKeywords(keywords, offset, pageSize);
        }
        return productRepository.listProducts(offset, pageSize);
    }

    public List<Product> getAllActiveProducts(Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return productRepository.findAvailableProductsByKeywords(keywords, offset, pageSize);
        }
        return productRepository.listAvailableProducts(offset, pageSize);
    }

    public long count() {
        return productRepository.count();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).get();
    }

    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

    public boolean productExistsById(UUID id) {
        return productRepository.existsById(id);
    }

    public Product saveProductFromApi(UUID productId, ProductRequest product) {
        Product ptmp = new Product();
        // are we creating a new product or updating an existing product?
        if (productId == null) {
            ptmp.setId(null);
        } else {
            ptmp.setId(productId);
            // check it exists
            if (!productExistsById(productId))
                throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        ptmp.setCode(product.getCode());
        ptmp.setName(product.getName());
        ptmp.setSummary(product.getSummary());
        ptmp.setDescription(product.getDescription());
        ptmp.setImage(product.getImage());
        ptmp.setPrice(product.getPrice());
        ptmp.setOnSale(product.getOnSale());
        ptmp.setSalePrice(product.getSalePrice());
        ptmp.setInStock(product.getInStock());
        ptmp.setTimeToStock(product.getTimeToStock());
        ptmp.setRating(product.getRating());
        ptmp.setAvailable(product.getAvailable());
        return productRepository.save(ptmp);
    }

    public Product saveProductFromAdminProductForm(AdminProductForm adminProductForm) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findByCode(adminProductForm.getCode());
        if (optionalProduct.isPresent()) {
            Product ptmp = optionalProduct.get();
            ptmp.setName(adminProductForm.getName());
            ptmp.setSummary(adminProductForm.getSummary());
            ptmp.setDescription(adminProductForm.getDescription());
            ptmp.setImage(adminProductForm.getImage());
            ptmp.setPrice(adminProductForm.getPrice());
            ptmp.setOnSale(adminProductForm.getOnSale());
            ptmp.setSalePrice(adminProductForm.getSalePrice());
            ptmp.setInStock(adminProductForm.getInStock());
            ptmp.setTimeToStock(adminProductForm.getTimeToStock());
            ptmp.setRating(adminProductForm.getRating());
            ptmp.setAvailable(adminProductForm.getAvailable());
            return ptmp;
        } else {
            throw new ProductNotFoundException("Product not found: " + adminProductForm.getCode());
        }
    }

    public Product newProductFormAdminNewProductForm(AdminNewProductForm adminNewProductForm) {
        Product ptmp = new Product();
        ptmp.setCode(adminNewProductForm.getCode());
        ptmp.setName(adminNewProductForm.getName());
        ptmp.setSummary(adminNewProductForm.getSummary());
        ptmp.setDescription(adminNewProductForm.getDescription());
        ptmp.setPrice(adminNewProductForm.getPrice());
        ptmp.setOnSale(adminNewProductForm.getOnSale());
        ptmp.setSalePrice(adminNewProductForm.getSalePrice());
        ptmp.setInStock(adminNewProductForm.getInStock());
        ptmp.setTimeToStock(adminNewProductForm.getTimeToStock());
        ptmp.setImage(adminNewProductForm.getImage());
        ptmp.setAvailable(adminNewProductForm.getAvailable());
        Product newProduct = productRepository.saveAndFlush(ptmp);
        return newProduct;
    }

    public Order newOrderFromOrderForm(OrderForm orderForm) {
        log.debug("newOrderFromOrderForm");
        Order otmp = new Order();
        otmp.setUser(orderForm.getUser());
        otmp.setCart(orderForm.getCart());
        otmp.setAmount(orderForm.getAmount());
        otmp.setOrderDate(new Date());
        Random r = new Random();
        int low = 10;
        int high = 100;
        int result = r.nextInt(high-low) + low;
        String formatted = String.format("%03d", result);
        log.debug("Setting order number to: " + "OID-P100-"+formatted);
        otmp.setOrderNum("OID-P100-"+formatted);
        log.debug(otmp.getOrderNum());
        Order newOrder = orderRepository.saveAndFlush(otmp);
        return newOrder;
    }

}
