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
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.Message;
import com.microfocus.example.exception.MessageNotFoundException;
import com.microfocus.example.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * A RESTFul controller for accessing message information.
 *
 * @author Kevin A. Lee
 */
@Api(description = "Retrieve, update, create and delete messages.", tags = {"messages"})
@RequestMapping(value = "/api/v1/messages")
@RestController
public class ApiMessageController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiMessageController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Find messages", tags = {"messages"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Message.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class)
    })
    @GetMapping(value = {"/",""}, produces = "application/json")
    public Iterable<Message> findMessages(
            @ApiParam("Keyword(s) search for messages to be found.")
            @RequestParam("keywords") Optional<String> keywords,
            @ApiParam("Page number of messages to start from.")
            @RequestParam("pageNum") Optional<Integer> pageNum) {
        if (keywords.equals(Optional.empty())) {
            log.debug("Retrieving all messages");
            return userService.getAllMessages();
        } else {
            Integer pNum = 1;
            if (!pageNum.equals(Optional.empty())) {
                pNum = pageNum.get();
            }
            log.debug("Retrieving messages with keywords: " + keywords);
            //return userService.listAll(pNum, keywords.get());
            //TODO: paging/searching
            return userService.getAllMessages();
        }
    }

    @ApiOperation(value = "Find a specific message by its Id", tags = {"messages"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Message.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Message not found", response = ApiErrorResponse.class),
    })
    @GetMapping(value = {"/{id}"}, produces = "application/json")
    public Optional<Message> findMessageById(
            @ApiParam(name = "id",
                    value = "Id of the message to be found. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable("id") Integer id) {
        log.debug("Retrieving message id: " + id);
        if (!userService.messageExistsById(id))
            throw new MessageNotFoundException("Message with id: " + id.toString() + " does not exist.");
        return userService.findMessageById(id);
    }

    @ApiOperation(value = "Create a new message", tags = {"messages"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Message.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 409, message = "Message already exists", response = ApiErrorResponse.class)
    })
    @PostMapping(value = {"/",""}, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Message newMessage(
            @ApiParam("Details of the message to be created. Cannot be empty.")
            @Valid @RequestBody Message newMessage) {
        newMessage.setId(0); // set to 0 for sequence id generation
        log.debug("Creating new message: " + newMessage.toString());
        return userService.saveMessage(newMessage);
    }

    @ApiOperation(value = "Update an existing message", tags = {"messages"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Message.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Message not found", response = ApiErrorResponse.class)
    })
    @PutMapping(value = {"/{id}"}, produces = "application/json")
    public Message updateMessage(
            @ApiParam("Details of the message to be updated. Cannot be empty.")
            @Valid @RequestBody Message newMessage,
            @ApiParam(name = "id",
                    value = "Id of the message to be updated. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Updating message id: " + id);
        return userService.saveMessage(newMessage);
    }

    @ApiOperation(value = "Delete a message", tags = {"messages"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Message not found", response = ApiErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    void deleteMessage(
            @ApiParam(name = "id",
                    value = "Id of the message to be deleted. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Deleting message id: " + id);
        userService.deleteMessageById(id);
    }

}


