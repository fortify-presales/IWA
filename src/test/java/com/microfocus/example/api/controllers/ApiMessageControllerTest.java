package com.microfocus.example.api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microfocus.example.exception.MessageNotFoundException;
import com.microfocus.example.payload.request.MessageRequest;
import com.microfocus.example.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ApiMessageController.class})
@ExtendWith(SpringExtension.class)
class ApiMessageControllerTest {
    @Autowired
    private ApiMessageController apiMessageController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link ApiMessageController#createMessage(MessageRequest)}
     */
    @Test
    void testCreateMessage() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setId(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setReadDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setSentDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        messageRequest.setText("Text");
        messageRequest.setUserId(UUID.randomUUID());
        String content = (new ObjectMapper()).writeValueAsString(messageRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ApiMessageController#createMessage(MessageRequest)}
     */
    @Test
    void testCreateMessage2() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setId(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setReadDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setSentDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        messageRequest.setText("Text");
        messageRequest.setUserId(UUID.randomUUID());
        String content = (new ObjectMapper()).writeValueAsString(messageRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/messages", "Uri Vars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ApiMessageController#createMessage(MessageRequest)}
     */
    @Test
    void testCreateMessage3() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setId(1);
        messageRequest.setReadDate(mock(java.sql.Date.class));
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setSentDate(java.util.Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        messageRequest.setText("Text");
        messageRequest.setUserId(UUID.randomUUID());
        String content = (new ObjectMapper()).writeValueAsString(messageRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ApiMessageController#deleteMessage(UUID)}
     */
    @Test
    void testDeleteMessage() throws Exception {
        doThrow(new MessageNotFoundException("An error occurred")).when(userService).deleteMessageById((UUID) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v3/messages/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiMessageController#getMessageById(UUID)}
     */
    @Test
    void testGetMessageById() throws Exception {
        when(userService.findMessageById((UUID) any())).thenThrow(new MessageNotFoundException("An error occurred"));
        when(userService.messageExistsById((UUID) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiMessageController#getMessagesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetMessagesByKeywords() throws Exception {
        when(userService.getAllMessages()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages");
        MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiMessageController#getMessagesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetMessagesByKeywords2() throws Exception {
        when(userService.getAllMessages()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages")
                .param("keywords", "foo");
        MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiMessageController#getMessagesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetMessagesByKeywords3() throws Exception {
        when(userService.getAllMessages()).thenThrow(new MessageNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiMessageController#getMessagesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetMessagesByKeywords4() throws Exception {
        when(userService.getAllMessages()).thenThrow(new MessageNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages")
                .param("keywords", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiMessageController#getUnreadMessageCountById(UUID)}
     */
    @Test
    void testGetUnreadMessageCountById() throws Exception {
        when(userService.getUserUnreadMessageCount((UUID) any())).thenReturn(3L);
        when(userService.userExistsById((UUID) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages/unread-count/{id}",
                UUID.randomUUID());
        MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("3"));
    }

    /**
     * Method under test: {@link ApiMessageController#getUnreadMessageCountById(UUID)}
     */
    @Test
    void testGetUnreadMessageCountById2() throws Exception {
        when(userService.getUserUnreadMessageCount((UUID) any()))
                .thenThrow(new MessageNotFoundException("An error occurred"));
        when(userService.userExistsById((UUID) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages/unread-count/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiMessageController#getUnreadMessageCountById(UUID)}
     */
    @Test
    void testGetUnreadMessageCountById3() throws Exception {
        when(userService.getUserUnreadMessageCount((UUID) any())).thenReturn(3L);
        when(userService.userExistsById((UUID) any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/messages/unread-count/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiMessageController#updateMessage(MessageRequest, UUID)}
     */
    @Test
    void testUpdateMessage() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setId(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setReadDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setSentDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        messageRequest.setText("Text");
        messageRequest.setUserId(UUID.randomUUID());
        String content = (new ObjectMapper()).writeValueAsString(messageRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v3/messages/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ApiMessageController#updateMessage(MessageRequest, UUID)}
     */
    @Test
    void testUpdateMessage2() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setId(1);
        messageRequest.setReadDate(mock(java.sql.Date.class));
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        messageRequest.setSentDate(java.util.Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        messageRequest.setText("Text");
        messageRequest.setUserId(UUID.randomUUID());
        String content = (new ObjectMapper()).writeValueAsString(messageRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v3/messages/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiMessageController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

