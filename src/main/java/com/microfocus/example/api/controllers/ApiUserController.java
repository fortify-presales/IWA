/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

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

import com.microfocus.example.entity.ApiStatusResponse;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * A RESTFul controller for accessing user information.
 *
 * @author Kevin A. Lee
 */
@RestController
@RequestMapping(value = "/api/v3/users")
@Tag(name = "users", description = "User operations")
public class ApiUserController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiUserController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Find users by keyword(s)", description = "Keyword search by %keyword% format", tags = {"users"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {""}, produces = {"application/json"})
    public ResponseEntity<List<User>> getUsersByKeywords(
            @Parameter(description = "Keyword(s) search for users to be found.") @RequestParam("keywords") Optional<String> keywords,
            @Parameter(description = "Offset of the starting record. 0 indicates the first record.") @RequestParam("offset") Optional<Integer> offset,
            @Parameter(description = "Maximum records to return. The maximum value allowed is 50.") @RequestParam("limit") Optional<Integer> limit) {
        log.debug("API::Retrieving users by keyword(s)");
        // TODO: implement keywords, offset and limit
        if (keywords.equals(Optional.empty())) {
            return ResponseEntity.ok().body(userService.getAllUsers());
        } else {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        }
    }

    @Operation(summary = "Find user by Id", description = "Find a specific user by its database Id", tags = {"users"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Message Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/{id}"}, produces =  {"application/json"})
    public ResponseEntity<User> findUserById(
            @Parameter(description = "Id of the user to be found. Cannot be empty.", example = "1", required = true) @PathVariable("id") Integer id) {
        log.debug("API::Retrieving user id: " + id);
        if (!userService.userExistsById(id))
            throw new UserNotFoundException("User with id: " + id.toString() + " does not exist.");
        Optional<User> user = userService.findUserById(id);
        return new ResponseEntity<>(user.orElse(null), HttpStatus.OK);
    }

    @Operation(summary = "Create a new user", description = "Creates a new user", tags = {"users"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "409", description = "User Already Exists", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PostMapping(value = {""}, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody User newUser) {
        newUser.setId(0); // set to 0 for sequence id generation
        log.debug("API::Creating new user: " + newUser.toString());
        return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.OK);
    }

    @Operation(summary = "Update a user", description = "Update an existing user", tags = {"users"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PutMapping(value = {"/{id}"}, produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<User> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody User newUser,
            @Parameter(description = "Id of the user to be updated. Cannot be empty.", example = "1", required = true) @PathVariable("id") Integer id) {
        log.debug("API::Updating user id: " + id);
        return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.OK);
    }

    @Operation(summary = "Delete a user", description = "Delete an existing user", tags = {"users"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class)))
    })
    @DeleteMapping (value = {"/{id}"})
    public ResponseEntity<ApiStatusResponse> deleteUser(
            @Parameter(description = "Id of the user to be updated. Cannot be empty.", example = "1", required = true) @PathVariable("id") Integer id) {
        log.debug("API@::Deleting user id: " + id);
        userService.deleteUserById(id);
        ApiStatusResponse apiStatusResponse = new ApiStatusResponse
                .ApiResponseBuilder()
                .withSuccess(true)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<>(apiStatusResponse, HttpStatus.OK);
    }

}


