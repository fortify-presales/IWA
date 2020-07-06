/*
        Secure Web App

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

import com.microfocus.example.entity.ApiErrorResponse;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * A RESTFul controller for accessing user information.
 *
 * @author Kevin A. Lee
 */
@Api(description = "Retrieve, update, create and delete users.", tags = {"users"})
@RequestMapping(value = "/api/v1")
@RestController
public class ApiUserController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiUserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Find users", tags = { "users" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class, responseContainer = "Iterable"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class)
    })
    @GetMapping(value = {"/users"}, produces = "application/json")
    public Iterable<User> findUsers(
            @ApiParam("Partial username of the user(s) to be found.")
            @RequestParam("username") Optional<String> partialName) {
        if (partialName.equals(Optional.empty())) {
            log.debug("Retrieving all users");
            return userService.getAllUsers();
        } else {
            log.debug("Retrieving users with username: " + partialName);
            return userService.findUsersByUsername(partialName.get());
        }
    }

    @ApiOperation(value = "Find a specific user by their Id", tags = { "users" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiErrorResponse.class),
    })
    @GetMapping(value = {"/users/{id}"}, produces = "application/json")
    public Optional<User> byId(
            @ApiParam(name = "id",
                    value = "Id of the user to be found. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable("id") Integer id) {
        log.debug("Retrieving user id: " + id);
        if (!userService.userExistsById(id)) throw new UserNotFoundException("User with id: " + id.toString() + " does not exist.");
        return userService.findUserById(id);
    }

    @ApiOperation(value = "Create a new user", tags = { "users" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 409, message = "User already exists", response = ApiErrorResponse.class)
    })
    @PostMapping(value = {"/users"}, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User newUser(
            @ApiParam("Details of the user to be created. Cannot be empty.")
            @Valid @RequestBody User newUser) {
        newUser.setId(0); // set to 0 for sequence id generation
        newUser.setPassword(EncryptedPasswordUtils.encryptPassword(newUser.getPassword()));
        log.debug("Creating new user: " + newUser.toString());
        return userService.saveUser(newUser);
    }

    @ApiOperation(value = "Update an existing user", tags = { "users" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiErrorResponse.class)
    })
    @PutMapping(value = {"/users/{id}"}, produces = "application/json")
    public User updateUser(
            @ApiParam("Details of the user to be updated. Cannot be empty.")
            @Valid @RequestBody User newUser,
            @ApiParam(name = "id",
                    value = "Id of the user to be updated. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Updating user id: " + id);
        return userService.saveUser(newUser);
    }

    @ApiOperation(value = "Delete a user", tags = { "users" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiErrorResponse.class)
    })
    @DeleteMapping(value = "/users/{id}", produces = "application/json")
    void deleteEmployee(
            @ApiParam(name = "id",
                    value = "Id of the user to be deleted. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Deleting user id: " + id);
        userService.deleteUserById(id);
    }

}


