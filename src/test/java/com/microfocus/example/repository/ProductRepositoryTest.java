package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ProductRepositoryTest extends BaseIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void productRepository_existsById() {
        if (!productRepository.existsById(1)) fail("No products exists");
    }

    @Test
    public void productRepository_listProductsPageable() {
        List<Product> products = productRepository.listProducts(1,1);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    public void productRepository_findProductById() {
        Optional<Product> p = productRepository.findById(1);
        if (p.isPresent())
            assertThat(p.get().getName()).isEqualTo("Solodox 750");
        else
            fail("Test product not found");
    }

}
