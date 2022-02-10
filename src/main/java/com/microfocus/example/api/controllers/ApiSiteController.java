/*
        Insecure Web App (IWA)

        Copyright (C) 2021 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.api.controllers;

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.User;
import com.microfocus.example.payload.request.LoginRequest;
import com.microfocus.example.payload.request.RegisterUserRequest;
import com.microfocus.example.payload.request.SubscribeUserRequest;
import com.microfocus.example.payload.response.ApiStatusResponse;
import com.microfocus.example.payload.response.JwtResponse;
import com.microfocus.example.payload.response.RegisterUserResponse;
import com.microfocus.example.payload.response.SubscribeUserResponse;
import com.microfocus.example.repository.RoleRepository;
import com.microfocus.example.repository.UserRepository;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A RESTFul controller for accessing site information.
 *
 * @author Kevin A. Lee
 */
@RestController
@RequestMapping(value = "/api/v3/site")
@Tag(name = "site", description = "Site operations")
public class ApiSiteController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiSiteController.class);

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Bean("ApiSiteControllerPasswordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    JwtUtils jwtUtils;

    public class SiteStatus {
        private String health;
        private String motd;

        SiteStatus() { }
        SiteStatus(String health, String motd) {
            this.health = health;
            this.motd = motd;
        }

        public String getHealth() {
            return health;
        }
        public void setHealth(String health) {
            this.health = health;
        }
        public String getMotd() {
            return motd;
        }
        public void setMotd(String motd) {
            this.motd = motd;
        }
    }

    @Operation(summary = "Get the site status", description = "Get the site message of the day", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SiteStatus.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/status"}, produces = {"application/json"})
    public ResponseEntity<SiteStatus> getSiteStatus() {
        log.debug("API::Retrieving Site Status");
        SiteStatus siteStatus = new SiteStatus("GREEN", "The site is currently healthy");
        return ResponseEntity.ok().body(siteStatus);
    }

    @Operation(summary = "Check if username is taken", description = "Check if a user with the specified username already exists in the site", tags = {"site"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/username-already-exists/{username}"}, produces =  {"application/json"})
    public ResponseEntity<Boolean> usernameIsTaken(
            @Parameter(description = "Username to check. Cannot be empty.", example = "user1", required = true) @PathVariable("username") String username) {
        log.debug("API::Checking for user with username: " + username);
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isPresent()) {
            return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.OK);
        }
    }

    @Operation(summary = "Check if email exists", description = "Check if a user with the specified email address already exists in the site", tags = {"site"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/email-already-exists/{email}"}, produces =  {"application/json"})
    public ResponseEntity<Boolean> emailIsTaken(
            @Parameter(description = "Email address to check. Cannot be empty.", example = "user1@localhost.com", required = true) @PathVariable("email") String email) {
        log.debug("API::Checking for user with email: " + email);
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.OK);
        }
    }

    @Operation(summary = "Register a new user", description = "Register a new user with the site", tags = {"site"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = RegisterUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "409", description = "User Already Exists", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PostMapping(value = {"/register-user"}, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiStatusResponse> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody RegisterUserRequest newUser) {
        log.debug("API::Registering new user: " + newUser.toString());
        RegisterUserResponse user = userService.registerUser(newUser);
        ApiStatusResponse response = new ApiStatusResponse();
        if (user.getEmail().equals(newUser.getEmail())) response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Subscribe a new user", description = "Subscribe a new user to the newsletter", tags = {"site"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = SubscribeUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "409", description = "User Already Exists", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PostMapping(value = {"/subscribe-user"}, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiStatusResponse> subscribeUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody SubscribeUserRequest newUser) {
        log.debug("API::Subscribing a user to the newsletter: " + newUser.toString());
        SubscribeUserResponse user = userService.subscribeUser(newUser);
        ApiStatusResponse response = new ApiStatusResponse();
        if ((user.getEmail().equals(newUser.getEmail())))  response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Sign in", description = "Sign in to the system", tags = {"site"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PostMapping(value = {"/sign-in"}, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetails iwaUser = (CustomUserDetails) authentication.getPrincipal();
        User user = iwaUser.getUserDetails();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                jwtUtils.getExpirationFromJwtToken(jwt),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles));
    }

}


