package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Product;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductRepositoryTest extends BaseIntegrationTest {

    public static final String PRODUCT_SEARCH_KEYWORDS = "alphadex";

    @Autowired
    ProductRepository productRepository;

    @Test
    public void a_productRepository_findProductByCode() {
        Optional<Product> p1 = productRepository.findByCode(DataSeeder.TEST_PRODUCT1_CODE);
        if (p1.isPresent())
            assertThat(p1.get().getName()).isEqualTo(DataSeeder.TEST_PRODUCT1_NAME);
        else
            fail("Test Product 1 not found");
    }

    @Test
    public void b_productRepository_findAll() {
        List<Product> products = productRepository.findAll(1,1);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    public void c_productRepository_new() {
        Product p1 = DataSeeder.generateProduct();
        p1 = productRepository.save(p1);
        if (!productRepository.existsById(p1.getId())) fail("Test Product 2 does not exist");
        Optional<Product> p2 = productRepository.findByCode(DataSeeder.TEST_PRODUCT2_CODE);
        if (p2.isPresent())
            assertThat(p2.get().getCode()).isEqualTo(p1.getCode());
        else
            fail("Test Product 2 not found");
    }

    @Test
    public void d_productRepository_update() {
        Optional<Product> optionalProduct = productRepository.findByCode(DataSeeder.TEST_PRODUCT2_CODE);
        if (optionalProduct.isPresent()) {
            assertThat(optionalProduct.get().getName()).isEqualTo(DataSeeder.TEST_PRODUCT2_NAME);
            Product p1 = optionalProduct.get();
            p1.setCode("TEST-B000-00001");
            p1.setName("Test Product 2 Updated");
            p1.setPrice(new Float(100.0));
            p1.setOnSale(false);
            p1.setRating(1);
            productRepository.save(p1);
            Optional<Product> p2 = productRepository.findByCode("TEST-B000-00001");
            if (p2.isPresent()) {
                assertThat(p2.get().getCode()).isEqualTo("TEST-B000-00001");
                assertThat(p2.get().getName()).isEqualTo("Test Product 2 Updated");
                assertThat(p2.get().getPrice()).isEqualTo(new Float(100.0));
                assertThat(p2.get().getRating()).isEqualTo(1);
            }
        }
    }

    @Test
    public void e_productRepository_findProductsByKeywords() {
        List<Product> products = productRepository.findByKeywords(PRODUCT_SEARCH_KEYWORDS, 1,1);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    public void f_productRepository_findAvailableByKeywords() {
        List<Product> products = productRepository.findByKeywords(PRODUCT_SEARCH_KEYWORDS, 1,1);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    public void g_productRepository_delete() {
        Optional<Product> optionalProduct = productRepository.findByCode(DataSeeder.TEST_PRODUCT2_CODE);
        if (optionalProduct.isPresent()) {
            UUID uuid = optionalProduct.get().getId();
            assertThat(productRepository.existsById(uuid)).isTrue();
            productRepository.deleteById(uuid);
            assertThat(productRepository.existsById(uuid)).isFalse();
        }
    }

}
