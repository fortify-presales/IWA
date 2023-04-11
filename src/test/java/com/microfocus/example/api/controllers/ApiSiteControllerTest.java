package com.microfocus.example.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.ApiSiteBadCredentialsException;
import com.microfocus.example.payload.request.LoginRequest;
import com.microfocus.example.payload.request.RegisterUserRequest;
import com.microfocus.example.payload.request.SubscribeUserRequest;
import com.microfocus.example.repository.RoleRepository;
import com.microfocus.example.repository.UserRepository;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.JwtUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ApiSiteController.class})
@ExtendWith(SpringExtension.class)
class ApiSiteControllerTest {
    @Autowired
    private ApiSiteController apiSiteController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link ApiSiteController#passwordEncoder()}
     */
    @Test
    void testPasswordEncoder() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     BCryptPasswordEncoder.BCRYPT_PATTERN
        //     BCryptPasswordEncoder.logger
        //     BCryptPasswordEncoder.random
        //     BCryptPasswordEncoder.strength

        apiSiteController.passwordEncoder();
    }

    /**
     * Method under test: {@link ApiSiteController#emailIsTaken(String)}
     */
    @Test
    void testEmailIsTaken() throws Exception {
        User user = new User();
        user.setAddress("42 Main St");
        user.setAuthorities(new HashSet<>());
        user.setCity("Oxford");
        user.setConfirmPassword("iloveyou");
        user.setCountry("GB");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setId(UUID.randomUUID());
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("4105551212");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");
        Optional<User> ofResult = Optional.of(user);
        when(userService.findUserByEmail((String) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v3/site/email-already-exists/{email}", "jane.doe@example.org");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406));
    }

    /**
     * Method under test: {@link ApiSiteController#registerUser(RegisterUserRequest)}
     */
    @Test
    void testRegisterUser() throws Exception {
        when(userService.registerUser((RegisterUserRequest) any()))
                .thenThrow(new ApiSiteBadCredentialsException("janedoe"));

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("jane.doe@example.org");
        registerUserRequest.setFirstName("Jane");
        registerUserRequest.setLastName("Doe");
        registerUserRequest.setPassword("iloveyou");
        registerUserRequest.setPhone("4105551212");
        registerUserRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(registerUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/site/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ApiSiteController.SiteStatus#SiteStatus(ApiSiteController)}
     *   <li>{@link ApiSiteController.SiteStatus#setHealth(String)}
     *   <li>{@link ApiSiteController.SiteStatus#setMotd(String)}
     *   <li>{@link ApiSiteController.SiteStatus#getHealth()}
     *   <li>{@link ApiSiteController.SiteStatus#getMotd()}
     * </ul>
     */
    @Test
    void testSiteStatusConstructor() {
        ApiSiteController.SiteStatus actualSiteStatus = (new ApiSiteController()).new SiteStatus();
        actualSiteStatus.setHealth("Health");
        actualSiteStatus.setMotd("Motd");
        assertEquals("Health", actualSiteStatus.getHealth());
        assertEquals("Motd", actualSiteStatus.getMotd());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ApiSiteController.SiteStatus#SiteStatus(ApiSiteController, String, String)}
     *   <li>{@link ApiSiteController.SiteStatus#setHealth(String)}
     *   <li>{@link ApiSiteController.SiteStatus#setMotd(String)}
     *   <li>{@link ApiSiteController.SiteStatus#getHealth()}
     *   <li>{@link ApiSiteController.SiteStatus#getMotd()}
     * </ul>
     */
    @Test
    void testSiteStatusConstructor2() {
        ApiSiteController.SiteStatus actualSiteStatus = (new ApiSiteController()).new SiteStatus("foo", "foo");
        actualSiteStatus.setHealth("Health");
        actualSiteStatus.setMotd("Motd");
        assertEquals("Health", actualSiteStatus.getHealth());
        assertEquals("Motd", actualSiteStatus.getMotd());
    }

    /**
     * Method under test: {@link ApiSiteController#subscribeUser(SubscribeUserRequest)}
     */
    @Test
    void testSubscribeUser() throws Exception {
        when(userService.subscribeUser((SubscribeUserRequest) any()))
                .thenThrow(new ApiSiteBadCredentialsException("janedoe"));

        SubscribeUserRequest subscribeUserRequest = new SubscribeUserRequest();
        subscribeUserRequest.setEmail("jane.doe@example.org");
        subscribeUserRequest.setFirstName("Jane");
        subscribeUserRequest.setId(1);
        subscribeUserRequest.setLastName("Doe");
        String content = (new ObjectMapper()).writeValueAsString(subscribeUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/site/subscribe-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiSiteController#emailIsTaken(String)}
     */
    @Test
    void testEmailIsTaken2() throws Exception {
        when(userService.findUserByEmail((String) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v3/site/email-already-exists/{email}", "jane.doe@example.org");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406));
    }

    /**
     * Method under test: {@link ApiSiteController#emailIsTaken(String)}
     */
    @Test
    void testEmailIsTaken3() throws Exception {
        when(userService.findUserByEmail((String) any())).thenThrow(new ApiSiteBadCredentialsException("janedoe"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v3/site/email-already-exists/{email}", "jane.doe@example.org");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiSiteController#getSiteStatus()}
     */
    @Test
    void testGetSiteStatus() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/site/status");
        MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"health\":\"GREEN\",\"motd\":\"The site is currently healthy\"}"));
    }

    /**
     * Method under test: {@link ApiSiteController#getSiteStatus()}
     */
    @Test
    void testGetSiteStatus2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v3/site/status");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"health\":\"GREEN\",\"motd\":\"The site is currently healthy\"}"));
    }

    /**
     * Method under test: {@link ApiSiteController#signIn(LoginRequest)}
     */
    @Test
    void testSignIn() throws Exception {
        when(jwtUtils.generateJwtToken((Authentication) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenThrow(new ApiSiteBadCredentialsException("janedoe"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/site/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiSiteController#signIn(LoginRequest)}
     */
    @Test
    void testSignIn2() throws Exception {
        when(jwtUtils.generateJwtToken((Authentication) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any())).thenThrow(new BadCredentialsException("?"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/site/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiSiteController#signIn(LoginRequest)}
     */
    @Test
    void testSignIn3() throws Exception {
        when(jwtUtils.getExpirationFromJwtToken((String) any())).thenReturn(1L);
        when(jwtUtils.generateJwtToken((Authentication) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken(new CustomUserDetails(new User()), "Credentials"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/site/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"username\":null,\"email\":null,\"roles\":[],\"tokenExpiration\":1,\"accessToken\":\"ABC123\",\"tokenType"
                                        + "\":\"Bearer\"}"));
    }

    /**
     * Method under test: {@link ApiSiteController#signIn(LoginRequest)}
     */
    @Test
    void testSignIn4() throws Exception {
        when(jwtUtils.getExpirationFromJwtToken((String) any())).thenThrow(new ApiSiteBadCredentialsException("janedoe"));
        when(jwtUtils.generateJwtToken((Authentication) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken(new CustomUserDetails(new User()), "Credentials"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/site/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiSiteController#usernameIsTaken(String)}
     */
    @Test
    void testUsernameIsTaken() throws Exception {
        User user = new User();
        user.setAddress("42 Main St");
        user.setAuthorities(new HashSet<>());
        user.setCity("Oxford");
        user.setConfirmPassword("iloveyou");
        user.setCountry("GB");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setId(UUID.randomUUID());
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("4105551212");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");
        Optional<User> ofResult = Optional.of(user);
        when(userService.findUserByUsername((String) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v3/site/username-already-exists/{username}", "janedoe");
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(Boolean.TRUE.toString()));
    }

    /**
     * Method under test: {@link ApiSiteController#usernameIsTaken(String)}
     */
    @Test
    void testUsernameIsTaken2() throws Exception {
        when(userService.findUserByUsername((String) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v3/site/username-already-exists/{username}", "janedoe");
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(apiSiteController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(Boolean.FALSE.toString()));
    }
}

