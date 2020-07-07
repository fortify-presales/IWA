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
import com.microfocus.example.entity.Authority;
import com.microfocus.example.exception.RoleNotFoundException;
import com.microfocus.example.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * A RESTFul controller for accessing role information.
 *
 * @author Kevin A. Lee
 */
@Api(description = "Retrieve, update, create and delete roles.", tags = {"roles"})
@RequestMapping(value = "/api/v1/roles")
@RestController
public class ApiRoleController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiRoleController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Find roles/authorities",
            notes = "",
            tags = {"roles"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Authority.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class)
    })
    @GetMapping(value = {"/", ""}, produces = "application/json")
    public Iterable<Authority> findRoles(
            @ApiParam("Partial name of the role(s) to be found.")
            @RequestParam("rolename") Optional<String> partialName) {
        if (partialName.equals(Optional.empty())) {
            log.debug("Retrieving all roles");
            return userService.getAllRoles();
        } else {
            log.debug("Retrieving roles with rolename: " + partialName);
            //return userService.findRolesByRolename(partialName.get());
            //TODO: paging/searching
            return userService.getAllRoles();
        }
    }

    @ApiOperation(value = "Find a specific role by its Id", tags = {"roles"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Authority.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Role not found", response = ApiErrorResponse.class),
    })
    @GetMapping(value = {"/{id}"}, produces = "application/json")
    public Optional<Authority> findRoleById(
            @ApiParam(name = "id",
                    value = "Id of the role/authority to be found. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable("id") Integer id) {
        log.debug("Retrieving role id: " + id);
        if (!userService.roleExistsById(id))
            throw new RoleNotFoundException("Role with id: " + id.toString() + " does not exist.");
        return userService.findRoleById(id);
    }

    @ApiOperation(value = "Create a new role", tags = {"roles"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Authority.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 409, message = "Role already exists", response = ApiErrorResponse.class)
    })
    @PostMapping(value = {"/", ""}, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Authority newRole(
            @ApiParam("Details of the role to be created. Cannot be empty.")
            @Valid @RequestBody Authority newRole) {
        newRole.setId(0); // set to 0 for sequence id generation
        log.debug("Creating new role: " + newRole.toString());
        return userService.saveRole(newRole);
    }

    @ApiOperation(value = "Update an existing role", tags = {"roles"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Authority.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Role not found", response = ApiErrorResponse.class)
    })
    @PutMapping(value = {"/{id}"}, produces = "application/json")
    public Authority updateRole(
            @ApiParam("Details of the role to be updated. Cannot be empty.")
            @Valid @RequestBody Authority newRole,
            @ApiParam(name = "id",
                    value = "Id of the role to be updated. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Updating role id: " + id);
        return userService.saveRole(newRole);
    }

    @ApiOperation(value = "Delete a role", tags = {"roles"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Role not found", response = ApiErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    void deleteRole(
            @ApiParam(name = "id",
                    value = "Id of the role to be deleted. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Deleting role id: " + id);
        userService.deleteRoleById(id);
    }

}


