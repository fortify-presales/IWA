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

package com.microfocus.example.service;

import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.Review;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.OrderNotFoundException;
import com.microfocus.example.exception.ProductNotFoundException;
import com.microfocus.example.exception.ReviewNotFoundException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.payload.request.OrderRequest;
import com.microfocus.example.payload.request.ProductRequest;
import com.microfocus.example.payload.request.ReviewRequest;
import com.microfocus.example.repository.OrderRepository;
import com.microfocus.example.repository.ProductRepository;
import com.microfocus.example.repository.ReviewRepository;
import com.microfocus.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Product Service for products and orders to hide business logs / database persistence
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
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.data.page-size:25}")
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Optional<Product> findProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll(1, productRepository.count());
    }

    public List<Product> getAllProducts(Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return productRepository.findByKeywords(keywords, offset, pageSize);
        } else {
            return productRepository.findAll(offset, pageSize);
        }
    }

    public List<Product> getAllActiveProducts(Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return productRepository.findAvailableByKeywords(keywords, offset, pageSize);
        }
        return productRepository.findAvailable(offset, pageSize);
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

    //
    // Reviews
    //

    public Optional<Review> findReviewById(UUID id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findReviewsByProductId(UUID productId) {
        return reviewRepository.findProductReviews(productId);
    }

    public List<Review> findReviewByUserId(UUID userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> getReviews() { return reviewRepository.findAll(); }

    public List<Review> getReviews(Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return reviewRepository.findReviewsByKeywords(keywords, offset, pageSize);
        } else {
            return reviewRepository.findReviews(offset, pageSize);
        }
    }

    public List<Review> getProductReviews(UUID pid) {
        return reviewRepository.findProductReviews(pid);
    }

    public List<Review> getProductReviews(UUID pid, Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return reviewRepository.findProductReviewsByKeywords(pid, keywords, offset, pageSize);
        } else {
            return reviewRepository.findProductReviews(pid);
        }
    }

    public boolean reviewExistsById(UUID id) {
        return reviewRepository.existsById(id);
    }

    public void deleteReviewById(UUID id) {
        reviewRepository.deleteById(id);
    }

    public Review saveReviewFromApi(UUID reviewId, ReviewRequest review) {
        Review rtmp = new Review();
        // are we creating a new review or updating an existing review?
        if (reviewId == null) {
            rtmp.setId(null);
            rtmp.setReviewDate(new Date());
        } else {
            rtmp.setId(reviewId);
            rtmp.setReviewDate(rtmp.getReviewDate());
            // check it exists
            if (!reviewExistsById(reviewId))
                throw new ReviewNotFoundException("Review not found with id: " + reviewId);
        }
        Optional<Product> optionalProduct = productRepository.findById(review.getProductId());
        if (optionalProduct.isPresent()) {
            rtmp.setProduct(optionalProduct.get());
        }
        Optional<User> optionalUser = userRepository.findById(review.getUserId());
        if (optionalUser.isPresent()) {
            rtmp.setUser(optionalUser.get());
        }
        rtmp.setComment(review.getComment());
        rtmp.setRating(review.getRating());
        return reviewRepository.save(rtmp);
    }

    //
    // Orders
    //

    public Optional<Order> findOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findOrderByNumber(String number) {
        return orderRepository.findByNumber(number);
    }

    public List<Order> getAllOrders() { return orderRepository.findAll(); }

    public List<Order> getAllOrders(Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return orderRepository.findOrdersByKeywords(keywords, offset, pageSize);
        }
        return orderRepository.listOrders(offset, pageSize);
    }

    public void deleteOrderById(UUID id) {
        orderRepository.deleteById(id);
    }

    public boolean orderExistsById(UUID id) {
        return orderRepository.existsById(id);
    }

    public Order saveOrderFromApi(UUID orderId, OrderRequest order) {
        Order otmp = new Order();
        // are we creating a new order or updating an existing order?
        if (orderId == null) {
            otmp.setId(null);
            otmp.setOrderDate(new Date());
        } else {
            otmp.setId(orderId);
            otmp.setOrderDate(otmp.getOrderDate());
            // check it exists
            if (!orderExistsById(orderId))
                throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
        otmp.setOrderNum(order.getOrderNum());
        otmp.setCart(order.getCart());
        otmp.setAmount(order.getAmount());
        Optional<User> optionalUser = userRepository.findById(order.getUserId());
        if (optionalUser.isPresent()) {
            otmp.setUser(optionalUser.get());
        }
        otmp.setShipped(order.getShipped());
        otmp.setShippedDate(order.getShippedDate());
        return orderRepository.save(otmp);
    }

}
