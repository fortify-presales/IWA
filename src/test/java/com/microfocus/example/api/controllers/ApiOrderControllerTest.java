package com.microfocus.example.api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.microfocus.example.exception.OrderNotFoundException;
import com.microfocus.example.payload.request.OrderRequest;
import com.microfocus.example.service.ProductService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ApiOrderController.class})
@ExtendWith(SpringExtension.class)
class ApiOrderControllerTest {
    @Autowired
    private ApiOrderController apiOrderController;

    @MockBean
    private ProductService productService;

    /**
     * Method under test: {@link ApiOrderController#findOrderById(UUID)}
     */
    @Test
    void testFindOrderById() throws Exception {
        when(productService.findOrderById((UUID) any())).thenThrow(new OrderNotFoundException("42"));
        when(productService.orderExistsById((UUID) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/orders/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiOrderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiOrderController#createOrder(OrderRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateOrder() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveOrderFromApi(java.util.UUID, com.microfocus.example.payload.request.OrderRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiOrderController.createOrder(ApiOrderController.java:130)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiOrderController apiOrderController = new ApiOrderController();
        apiOrderController.createOrder(new OrderRequest());
    }

    /**
     * Method under test: {@link ApiOrderController#createOrder(OrderRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateOrder2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.payload.request.OrderRequest.toString()" because "newOrder" is null
        //       at com.microfocus.example.api.controllers.ApiOrderController.createOrder(ApiOrderController.java:129)
        //   See https://diff.blue/R013 to resolve this issue.

        (new ApiOrderController()).createOrder(null);
    }

    /**
     * Method under test: {@link ApiOrderController#createOrder(OrderRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateOrder3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveOrderFromApi(java.util.UUID, com.microfocus.example.payload.request.OrderRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiOrderController.createOrder(ApiOrderController.java:130)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiOrderController apiOrderController = new ApiOrderController();

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderDate(mock(Date.class));
        apiOrderController.createOrder(orderRequest);
    }

    /**
     * Method under test: {@link ApiOrderController#updateOrder(OrderRequest, UUID)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateOrder() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveOrderFromApi(java.util.UUID, com.microfocus.example.payload.request.OrderRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiOrderController.updateOrder(ApiOrderController.java:147)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiOrderController apiOrderController = new ApiOrderController();
        OrderRequest newOrder = new OrderRequest();
        apiOrderController.updateOrder(newOrder, UUID.randomUUID());
    }

    /**
     * Method under test: {@link ApiOrderController#updateOrder(OrderRequest, UUID)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateOrder2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveOrderFromApi(java.util.UUID, com.microfocus.example.payload.request.OrderRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiOrderController.updateOrder(ApiOrderController.java:147)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiOrderController apiOrderController = new ApiOrderController();

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderDate(mock(Date.class));
        apiOrderController.updateOrder(orderRequest, UUID.randomUUID());
    }

    /**
     * Method under test: {@link ApiOrderController#deleteOrder(UUID)}
     */
    @Test
    void testDeleteOrder() throws Exception {
        doThrow(new OrderNotFoundException("42")).when(productService).deleteOrderById((UUID) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v3/orders/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiOrderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiOrderController#getOrdersByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetOrdersByKeywords() throws Exception {
        when(productService.getAllOrders()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/orders");
        MockMvcBuilders.standaloneSetup(apiOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiOrderController#getOrdersByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetOrdersByKeywords2() throws Exception {
        when(productService.getAllOrders((Integer) any(), (String) any())).thenReturn(new ArrayList<>());
        when(productService.getAllOrders()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/orders")
                .param("keywords", "foo");
        MockMvcBuilders.standaloneSetup(apiOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiOrderController#getOrdersByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetOrdersByKeywords3() throws Exception {
        when(productService.getAllOrders((Integer) any(), (String) any())).thenThrow(new OrderNotFoundException("42"));
        when(productService.getAllOrders()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/orders")
                .param("keywords", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiOrderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

