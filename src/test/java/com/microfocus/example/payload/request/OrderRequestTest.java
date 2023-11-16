package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.User;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class OrderRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OrderRequest#OrderRequest()}
     *   <li>{@link OrderRequest#setAmount(float)}
     *   <li>{@link OrderRequest#setCart(String)}
     *   <li>{@link OrderRequest#setId(UUID)}
     *   <li>{@link OrderRequest#setOrderDate(Date)}
     *   <li>{@link OrderRequest#setOrderNum(String)}
     *   <li>{@link OrderRequest#setShipped(Boolean)}
     *   <li>{@link OrderRequest#setShippedDate(Date)}
     *   <li>{@link OrderRequest#setUserId(UUID)}
     *   <li>{@link OrderRequest#toString()}
     *   <li>{@link OrderRequest#getAmount()}
     *   <li>{@link OrderRequest#getCart()}
     *   <li>{@link OrderRequest#getId()}
     *   <li>{@link OrderRequest#getOrderDate()}
     *   <li>{@link OrderRequest#getOrderNum()}
     *   <li>{@link OrderRequest#getShipped()}
     *   <li>{@link OrderRequest#getShippedDate()}
     *   <li>{@link OrderRequest#getUserId()}
     * </ul>
     */
    @Test
    void testConstructor() {
        OrderRequest actualOrderRequest = new OrderRequest();
        actualOrderRequest.setAmount(10.0f);
        actualOrderRequest.setCart("Cart");
        UUID randomUUIDResult = UUID.randomUUID();
        actualOrderRequest.setId(randomUUIDResult);
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualOrderRequest.setOrderDate(fromResult);
        actualOrderRequest.setOrderNum("Name");
        actualOrderRequest.setShipped(true);
        Date fromResult1 = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualOrderRequest.setShippedDate(fromResult1);
        UUID randomUUIDResult1 = UUID.randomUUID();
        actualOrderRequest.setUserId(randomUUIDResult1);
        actualOrderRequest.toString();
        assertEquals(10.0f, actualOrderRequest.getAmount());
        assertEquals("Cart", actualOrderRequest.getCart());
        assertSame(randomUUIDResult, actualOrderRequest.getId());
        assertSame(fromResult, actualOrderRequest.getOrderDate());
        assertNull(actualOrderRequest.getOrderNum());
        assertTrue(actualOrderRequest.getShipped());
        assertSame(fromResult1, actualOrderRequest.getShippedDate());
        assertSame(randomUUIDResult1, actualOrderRequest.getUserId());
    }

    /**
     * Method under test: {@link OrderRequest#OrderRequest(Order)}
     */
    @Test
    void testConstructor2() {
        User user = new User();
        user.setAddress("42 Main St");
        user.setAuthorities(new HashSet<>());
        user.setCity("Oxford");
        user.setConfirmPassword("iloveyou");
        user.setCountry("GB");
        user.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        UUID randomUUIDResult = UUID.randomUUID();
        user.setId(randomUUIDResult);
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");

        Order order = new Order();
        order.setAmount(10.0f);
        order.setCart("Cart");
        order.setId(UUID.randomUUID());
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        order.setOrderDate(fromResult);
        order.setOrderNum("Order Num");
        order.setShipped(true);
        Date fromResult1 = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        order.setShippedDate(fromResult1);
        order.setUser(user);
        OrderRequest actualOrderRequest = new OrderRequest(order);
        assertEquals(10.0f, actualOrderRequest.getAmount());
        assertSame(randomUUIDResult, actualOrderRequest.getUserId());
        assertSame(fromResult1, actualOrderRequest.getShippedDate());
        assertEquals("Cart", actualOrderRequest.getCart());
        assertSame(fromResult, actualOrderRequest.getOrderDate());
        assertEquals("Order Num", actualOrderRequest.getOrderNum());
        assertTrue(actualOrderRequest.getShipped());
    }
}

