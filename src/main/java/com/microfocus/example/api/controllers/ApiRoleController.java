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

import com.microfocus.example.payload.response.ApiStatusResponse;
import com.microfocus.example.entity.Authority;
import com.microfocus.example.exception.RoleNotFoundException;
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
 * A RESTFul controller for accessing role information.
 *
 * @author Kevin A. Lee
 */
@RestController
@RequestMapping(value = "/api/v3/roles")
@Tag(name = "roles", description = "Role operations")
public class ApiRoleController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiRoleController.class);

    @Autowired
    private UserService roleService;

    @Operation(summary = "Find roles by keyword(s)", description = "Keyword search by %keyword% format", tags = {"roles"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Authority.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {""}, produces = {"application/json"})
    public ResponseEntity<List<Authority>> getRolesByKeywords(
            @Parameter(description = "Keyword(s) search for roles to be found.") @RequestParam("keywords") Optional<String> keywords,
            @Parameter(description = "Offset of the starting record. 0 indicates the first record.") @RequestParam("offset") Optional<Integer> offset,
            @Parameter(description = "Maximum records to return. The maximum value allowed is 50.") @RequestParam("limit") Optional<Integer> limit) {
        log.debug("API::Retrieving roles by keyword(s)");
        // TODO: implement keywords, offset and limit
        if (keywords.equals(Optional.empty())) {
            return ResponseEntity.ok().body(roleService.getAllRoles());
        } else {
            return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
        }
    }

    @Operation(summary = "Find role Id", description = "Find a specific role by its UUID", tags = {"roles"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Authority.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Message Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/{id}"}, produces =  {"application/json"})
    public ResponseEntity<Authority> findRoleById(
            @Parameter(description = "UUID of the role to be found. Cannot be empty.", example = "6bdd6188-d659-4390-8d37-8f090d2ed69a", required = true) @PathVariable("id") Integer id) {
        log.debug("API::Retrieving role with UUID: " + id);
        if (!roleService.roleExistsById(id))
            throw new RoleNotFoundException("Role with UUID: " + id.toString() + " does not exist.");
        Optional<Authority> role = roleService.findRoleById(id);
        return new ResponseEntity<>(role.orElse(null), HttpStatus.OK);
    }

    @Operation(summary = "Create a new role", description = "Creates a new role", tags = {"roles"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Authority.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "409", description = "Role Already Exists", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PostMapping(value = {""}, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Authority> createRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody Authority newRole) {
        //newRole.setId(0); // set to 0 for sequence id generation
        log.debug("API::Creating new role: " + newRole.toString());
        return new ResponseEntity<>(roleService.saveRole(newRole), HttpStatus.OK);
    }

    @Operation(summary = "Update a role", description = "Update an existing role", tags = {"roles"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Authority.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Role Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PutMapping(value = {"/{id}"}, produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Authority> updateRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody Authority newRole,
            @Parameter(description = "UUID of the role to be updated. Cannot be empty.", example = "6bdd6188-d659-4390-8d37-8f090d2ed69a", required = true) @PathVariable("id") Integer id) {
        log.debug("API::Updating role with UUID: " + id);
        return new ResponseEntity<>(roleService.saveRole(newRole), HttpStatus.OK);
    }

    @Operation(summary = "Delete a role", description = "Delete a role", tags = {"roles"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class)))
    })
    @DeleteMapping (value = {"/{id}"})
    public ResponseEntity<ApiStatusResponse> deleteRole(
            @Parameter(description = "UUID of the role to be updated. Cannot be empty.", example = "6bdd6188-d659-4390-8d37-8f090d2ed69a", required = true) @PathVariable("id") Integer id) {
        log.debug("API::Deleting role with UUID: " + id);
        roleService.deleteRoleById(id);
        ApiStatusResponse apiStatusResponse = new ApiStatusResponse
                .ApiResponseBuilder()
                .withSuccess(true)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<>(apiStatusResponse, HttpStatus.OK);
    }

}


