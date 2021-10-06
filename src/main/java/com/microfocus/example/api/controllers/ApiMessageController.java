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

import com.microfocus.example.payload.request.MessageRequest;
import com.microfocus.example.payload.response.ApiStatusResponse;
import com.microfocus.example.entity.Message;
import com.microfocus.example.exception.MessageNotFoundException;
import com.microfocus.example.service.UserService;
import com.microfocus.example.payload.response.MessageResponse;
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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A RESTFul controller for accessing message information.
 *
 * @author Kevin A. Lee
 */
@RestController
@RequestMapping(value = "/api/v3/messages")
@Tag(name = "messages", description = "User message operations")
public class ApiMessageController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiMessageController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Finds messages by keyword(s)", description = "Keyword search by %keyword% format", tags = {"message"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {""}, produces = {"application/json"})
    public ResponseEntity<List<MessageResponse>> getMessagesByKeywords(
            @Parameter(description = "Keyword(s) search for messages to be found.") @RequestParam("keywords") Optional<String> keywords,
            @Parameter(description = "Offset of the starting record. 0 indicates the first record.") @RequestParam("offset") Optional<Integer> offset,
            @Parameter(description = "Maximum records to return. The maximum value allowed is 50.") @RequestParam("limit") Optional<Integer> limit) {
        log.debug("API::Retrieving messages by keyword(s)");
        // TODO: implement keywords, offset and limit
        if (keywords.equals(Optional.empty())) {
            return ResponseEntity.ok().body(
                userService.getAllMessages().stream()
                    .map(MessageResponse::new)
                    .collect(Collectors.toList()));
        } else {
            return new ResponseEntity<>(
                userService.getAllMessages().stream()
                    .map(MessageResponse::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    @Operation(summary = "Find message by Id", description = "Find a message by UUID", tags = {"message"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Message Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/{id}"}, produces =  {"application/json"})
    public ResponseEntity<MessageResponse> getMessageById(
            @Parameter(description = "UUID of the message to be found. Cannot be empty.", example = "6914e47d-2f0a-4deb-a712-12e7801e13e8", required = true) @PathVariable("id") UUID id) {
        log.debug("API::Retrieving message with UUID: " + id);
        if (!userService.messageExistsById(id))
            throw new MessageNotFoundException("Message with id: " + id.toString() + " does not exist.");
        Optional<Message> message = userService.findMessageById(id);
        return message.map(value -> new ResponseEntity<>(new MessageResponse(value), HttpStatus.OK)).orElse(null);
    }

    @Operation(summary = "Create a new message", description = "Creates a new message for a user", tags = {"messages"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "409", description = "Message Already Exists", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PostMapping(value = {""}, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MessageResponse> createMessage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody MessageRequest newMessage) {
        log.debug("API::Creating new message: " + newMessage.toString());
        return new ResponseEntity<>(new MessageResponse(userService.saveMessageFromApi(null, newMessage)), HttpStatus.OK);
    }

    @Operation(summary = "Update a message", description = "Update a users message", tags = {"messages"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Message Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PutMapping(value = {"/{id}"}, produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<MessageResponse> updateMessage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody MessageRequest newMessage,
            @Parameter(description = "UUID of the message to be updated. Cannot be empty.", example = "6914e47d-2f0a-4deb-a712-12e7801e13e8", required = true) @PathVariable("id") UUID id) {
        log.debug("API::Updating message with UUID: " + id);
        return new ResponseEntity<>(new MessageResponse(userService.saveMessageFromApi(id, newMessage)), HttpStatus.OK);
    }

    @Operation(summary = "Delete a message", description = "Delete a users existing message", tags = {"messages"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Message Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class)))
    })
    @DeleteMapping (value = {"/{id}"})
    public ResponseEntity<ApiStatusResponse> deleteMessage(
            @Parameter(description = "UUID of the message to be updated. Cannot be empty.", example = "6914e47d-2f0a-4deb-a712-12e7801e13e8", required = true) @PathVariable("id") UUID id) {
        log.debug("API::Deleting message with UUID: " + id);
        userService.deleteMessageById(id);
        ApiStatusResponse apiStatusResponse = new ApiStatusResponse
                .ApiResponseBuilder()
                    .withSuccess(true)
                    .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<>(apiStatusResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get users unread message count", description = "Get a users unread message count by their UUID", tags = {"message"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/unread-count/{id}"}, produces =  {"application/json"})
    public ResponseEntity<Long> getUnreadMessageCountById(
            @Parameter(description = "UUID of the user to find messages for. Cannot be empty.", example = "32e7db01-86bc-4687-9ecb-d79b265ac14f", required = true) @PathVariable("id") UUID id) {
        log.debug("API::Retrieving unread message count for user with UUID: " + id);
        if (!userService.userExistsById(id))
            throw new MessageNotFoundException("User with id: " + id.toString() + " does not exist.");
        return new ResponseEntity<Long>(userService.getUserUnreadMessageCount(id), HttpStatus.OK);

    }

}


