package com.microfocus.example.api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.AuthorityType;
import com.microfocus.example.exception.RoleNotFoundException;
import com.microfocus.example.service.UserService;

import java.util.ArrayList;

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

@ContextConfiguration(classes = {ApiRoleController.class})
@ExtendWith(SpringExtension.class)
class ApiRoleControllerTest {
    @Autowired
    private ApiRoleController apiRoleController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link ApiRoleController#createRole(Authority)}
     */
    @Test
    void testCreateRole() throws Exception {
        when(userService.saveRole((Authority) any())).thenThrow(new RoleNotFoundException("42"));

        Authority authority = new Authority();
        authority.setId(UUID.randomUUID());
        authority.setName(AuthorityType.ROLE_ADMIN);
        String content = (new ObjectMapper()).writeValueAsString(authority);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiRoleController#findRoleById(Integer)}
     */
    @Test
    void testFindRoleById() throws Exception {
        when(userService.findRoleById((Integer) any())).thenThrow(new RoleNotFoundException("42"));
        when(userService.roleExistsById((Integer) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/roles/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiRoleController#findRoleById(Integer)}
     */
    @Test
    void testFindRoleById2() throws Exception {
        Authority authority = new Authority();
        authority.setId(UUID.randomUUID());
        authority.setName(AuthorityType.ROLE_ADMIN);
        Optional<Authority> ofResult = Optional.of(authority);
        when(userService.findRoleById((Integer) any())).thenReturn(ofResult);
        when(userService.roleExistsById((Integer) any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/roles/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiRoleController#deleteRole(Integer)}
     */
    @Test
    void testDeleteRole() throws Exception {
        doThrow(new RoleNotFoundException("42")).when(userService).deleteRoleById((Integer) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v3/roles/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiRoleController#getRolesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetRolesByKeywords() throws Exception {
        when(userService.getAllRoles()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/roles");
        MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiRoleController#getRolesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetRolesByKeywords2() throws Exception {
        when(userService.getAllRoles()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/roles")
                .param("keywords", "foo");
        MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiRoleController#getRolesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetRolesByKeywords3() throws Exception {
        when(userService.getAllRoles()).thenThrow(new RoleNotFoundException("42"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/roles");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiRoleController#getRolesByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetRolesByKeywords4() throws Exception {
        when(userService.getAllRoles()).thenThrow(new RoleNotFoundException("42"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/roles")
                .param("keywords", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiRoleController#updateRole(Authority, Integer)}
     */
    @Test
    void testUpdateRole() throws Exception {
        when(userService.saveRole((Authority) any())).thenThrow(new RoleNotFoundException("42"));

        Authority authority = new Authority();
        authority.setId(UUID.randomUUID());
        authority.setName(AuthorityType.ROLE_ADMIN);
        String content = (new ObjectMapper()).writeValueAsString(authority);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v3/roles/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiRoleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

