package com.microfocus.example.service;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Product;
import com.microfocus.example.web.form.admin.AdminProductForm;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductServiceTest extends BaseIntegrationTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductService userService;

    @Test
    public void a_productService_findById() {
        Optional<Product> p = productService.findProductById(DataSeeder.TEST_PRODUCT1_ID);
        if (p.isPresent())
            assertThat(p.get().getCode()).isEqualTo(DataSeeder.TEST_PRODUCT1_CODE);
        else
            fail("Test Product 1 not found");
    }

    @Test
    public void b_productService_findUserByCode() {
        Optional<Product> p = productService.findProductByCode(DataSeeder.TEST_PRODUCT1_CODE);
        if (p.isPresent())
            assertThat(p.get().getId()).isEqualTo(DataSeeder.TEST_PRODUCT1_ID);
        else
            fail("Test User 1 not found");
    }

    @Test
    public void d_productService_save() {
        Optional<Product> optionalProduct = productService.findProductById(DataSeeder.TEST_PRODUCT1_ID);
        if (optionalProduct.isPresent()) {
            AdminProductForm productForm = new AdminProductForm(optionalProduct.get());
            productForm.setCode("ABCDEF-12345");
            productForm.setName(DataSeeder.TEST_PRODUCT1_NAME + " Updated");
            try {
                productService.saveProductFromAdminProductForm(productForm);
                Optional<Product> updatedProduct = productService.findProductByCode("ABCDEF-12345");
                updatedProduct.ifPresent(user -> assertThat(user.getName()).isEqualTo(DataSeeder.TEST_PRODUCT1_NAME + " Updated"));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else
            fail("Test Product 1 not found");
    }

}
