package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class OrderTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Order#Order(UUID, User, Date, String, float, String, boolean)}
     *   <li>{@link Order#setAmount(float)}
     *   <li>{@link Order#setCart(String)}
     *   <li>{@link Order#setId(UUID)}
     *   <li>{@link Order#setOrderDate(Date)}
     *   <li>{@link Order#setOrderNum(String)}
     *   <li>{@link Order#setShipped(Boolean)}
     *   <li>{@link Order#setShippedDate(Date)}
     *   <li>{@link Order#setUser(User)}
     *   <li>{@link Order#toString()}
     *   <li>{@link Order#getAmount()}
     *   <li>{@link Order#getCart()}
     *   <li>{@link Order#getId()}
     *   <li>{@link Order#getOrderDate()}
     *   <li>{@link Order#getOrderNum()}
     *   <li>{@link Order#getShipped()}
     *   <li>{@link Order#getShippedDate()}
     *   <li>{@link Order#getUser()}
     * </ul>
     */
    @Test
    void testConstructor() {
        UUID id = UUID.randomUUID();

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
        user.setId(UUID.randomUUID());
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");
        Order actualOrder = new Order(id, user,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()), "Order Num", 10.0f,
                "Cart", true);
        actualOrder.setAmount(10.0f);
        actualOrder.setCart("Cart");
        UUID randomUUIDResult = UUID.randomUUID();
        actualOrder.setId(randomUUIDResult);
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualOrder.setOrderDate(fromResult);
        actualOrder.setOrderNum("Order Num");
        actualOrder.setShipped(true);
        Date fromResult1 = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualOrder.setShippedDate(fromResult1);
        User user1 = new User();
        user1.setAddress("42 Main St");
        user1.setAuthorities(new HashSet<>());
        user1.setCity("Oxford");
        user1.setConfirmPassword("iloveyou");
        user1.setCountry("GB");
        user1.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user1.setEmail("jane.doe@example.org");
        user1.setEnabled(true);
        user1.setFirstName("Jane");
        user1.setId(UUID.randomUUID());
        user1.setLastName("Doe");
        user1.setMfa(true);
        user1.setPassword("iloveyou");
        user1.setPhone("6625550144");
        user1.setState("MD");
        user1.setUsername("janedoe");
        user1.setVerifyCode("Verify Code");
        user1.setZip("21654");
        actualOrder.setUser(user1);
        actualOrder.toString();
        assertEquals(10.0f, actualOrder.getAmount());
        assertEquals("Cart", actualOrder.getCart());
        assertSame(randomUUIDResult, actualOrder.getId());
        assertSame(fromResult, actualOrder.getOrderDate());
        assertEquals("Order Num", actualOrder.getOrderNum());
        assertTrue(actualOrder.getShipped());
        assertSame(fromResult1, actualOrder.getShippedDate());
        assertSame(user1, actualOrder.getUser());
    }

    /**
     * Method under test: {@link Order#Order()}
     */
    @Test
    void testConstructor2() {
        assertFalse((new Order()).getShipped());
    }
}

