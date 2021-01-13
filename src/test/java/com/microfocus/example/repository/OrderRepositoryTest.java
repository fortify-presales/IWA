package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Order;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderRepositoryTest extends BaseIntegrationTest {

    @Autowired
    OrderRepository OrderRepository;

    @Test
    public void a_OrderRepository_existsById() {
        if (!OrderRepository.existsById(DataSeeder.TEST_ORDER1_ID)) fail("Order "+ DataSeeder.TEST_ORDER1_ID + " does not exist");
    }

    @Test
    public void b_OrderRepository_findOrderById() {
        Optional<Order> m = OrderRepository.findById(DataSeeder.TEST_ORDER1_ID);
        if (m.isPresent())
            assertThat(m.get().getOrderNum()).isEqualTo(DataSeeder.TEST_ORDER1_ORDER_NUM);
        else
            fail("Test Order 1 not found");
    }

    @Test
    public void c_OrderRepository_findOrderByUserId() {
        List<Order> Orders = OrderRepository.findByUserId(DataSeeder.TEST_ORDER1_USERID);
        assertThat(Orders.size()).isEqualTo(1);
    }

}
