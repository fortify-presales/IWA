package com.microfocus.example.api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.microfocus.example.exception.ReviewNotFoundException;
import com.microfocus.example.payload.request.ReviewRequest;
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

@ContextConfiguration(classes = {ApiReviewController.class})
@ExtendWith(SpringExtension.class)
class ApiReviewControllerTest {
    @Autowired
    private ApiReviewController apiReviewController;

    @MockBean
    private ProductService productService;

    /**
     * Method under test: {@link ApiReviewController#findReviewById(UUID)}
     */
    @Test
    void testFindReviewById() throws Exception {
        when(productService.findReviewById((UUID) any())).thenThrow(new ReviewNotFoundException("42"));
        when(productService.reviewExistsById((UUID) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/reviews/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiReviewController#createReview(ReviewRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateReview() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveReviewFromApi(java.util.UUID, com.microfocus.example.payload.request.ReviewRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiReviewController.createReview(ApiReviewController.java:131)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiReviewController apiReviewController = new ApiReviewController();
        apiReviewController.createReview(new ReviewRequest());
    }

    /**
     * Method under test: {@link ApiReviewController#createReview(ReviewRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateReview2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.payload.request.ReviewRequest.toString()" because "newReview" is null
        //       at com.microfocus.example.api.controllers.ApiReviewController.createReview(ApiReviewController.java:130)
        //   See https://diff.blue/R013 to resolve this issue.

        (new ApiReviewController()).createReview(null);
    }

    /**
     * Method under test: {@link ApiReviewController#createReview(ReviewRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateReview3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveReviewFromApi(java.util.UUID, com.microfocus.example.payload.request.ReviewRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiReviewController.createReview(ApiReviewController.java:131)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiReviewController apiReviewController = new ApiReviewController();

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setReviewDate(mock(Date.class));
        apiReviewController.createReview(reviewRequest);
    }

    /**
     * Method under test: {@link ApiReviewController#updateReview(ReviewRequest, UUID)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateReview() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveReviewFromApi(java.util.UUID, com.microfocus.example.payload.request.ReviewRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiReviewController.updateReview(ApiReviewController.java:148)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiReviewController apiReviewController = new ApiReviewController();
        ReviewRequest newReview = new ReviewRequest();
        apiReviewController.updateReview(newReview, UUID.randomUUID());
    }

    /**
     * Method under test: {@link ApiReviewController#updateReview(ReviewRequest, UUID)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateReview2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.service.ProductService.saveReviewFromApi(java.util.UUID, com.microfocus.example.payload.request.ReviewRequest)" because "this.productService" is null
        //       at com.microfocus.example.api.controllers.ApiReviewController.updateReview(ApiReviewController.java:148)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiReviewController apiReviewController = new ApiReviewController();

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setReviewDate(mock(Date.class));
        apiReviewController.updateReview(reviewRequest, UUID.randomUUID());
    }

    /**
     * Method under test: {@link ApiReviewController#deleteReview(UUID)}
     */
    @Test
    void testDeleteReview() throws Exception {
        doNothing().when(productService).deleteReviewById((UUID) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v3/reviews/{id}",
                UUID.randomUUID());
        MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"success\":true,\"timestamp\":\"2023-04-07 11:41:56\",\"errors\":null}"));
    }

    /**
     * Method under test: {@link ApiReviewController#deleteReview(UUID)}
     */
    @Test
    void testDeleteReview2() throws Exception {
        doThrow(new ReviewNotFoundException("42")).when(productService).deleteReviewById((UUID) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v3/reviews/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiReviewController#deleteReview(UUID)}
     */
    @Test
    void testDeleteReview3() throws Exception {
        doNothing().when(productService).deleteReviewById((UUID) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/v3/reviews/{id}",
                UUID.randomUUID());
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"success\":true,\"timestamp\":\"2023-04-07 11:41:56\",\"errors\":null}"));
    }

    /**
     * Method under test: {@link ApiReviewController#getReviewsByKeywords(Optional, Optional, Optional, Optional)}
     */
    @Test
    void testGetReviewsByKeywords() throws Exception {
        when(productService.getReviews((Integer) any(), (String) any())).thenReturn(new ArrayList<>());
        doNothing().when(productService).setPageSize((Integer) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/reviews");
        MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiReviewController#getReviewsByKeywords(Optional, Optional, Optional, Optional)}
     */
    @Test
    void testGetReviewsByKeywords2() throws Exception {
        when(productService.getReviews((Integer) any(), (String) any())).thenReturn(new ArrayList<>());
        doNothing().when(productService).setPageSize((Integer) any());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v3/reviews");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("limit", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiReviewController#getReviewsByKeywords(Optional, Optional, Optional, Optional)}
     */
    @Test
    void testGetReviewsByKeywords3() throws Exception {
        when(productService.getReviews((Integer) any(), (String) any())).thenThrow(new ReviewNotFoundException("42"));
        doThrow(new ReviewNotFoundException("42")).when(productService).setPageSize((Integer) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/reviews");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiReviewController#getReviewsByKeywords(Optional, Optional, Optional, Optional)}
     */
    @Test
    void testGetReviewsByKeywords4() throws Exception {
        when(productService.getReviews((Integer) any(), (String) any())).thenThrow(new ReviewNotFoundException("42"));
        doThrow(new ReviewNotFoundException("42")).when(productService).setPageSize((Integer) any());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v3/reviews");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("limit", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiReviewController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

