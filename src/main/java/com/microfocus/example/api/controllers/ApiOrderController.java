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

import com.microfocus.example.entity.Order;
import com.microfocus.example.exception.OrderNotFoundException;
import com.microfocus.example.payload.request.OrderRequest;
import com.microfocus.example.payload.response.ApiStatusResponse;
import com.microfocus.example.payload.response.OrderResponse;
import com.microfocus.example.payload.response.ProductResponse;
import com.microfocus.example.service.ProductService;
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
 * A RESTFul controller for accessing order information.
 *
 * @author Kevin A. Lee
 */
@RestController
@RequestMapping(value = "/api/v3/orders")
@Tag(name = "orders", description = "Order operations")
public class ApiOrderController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiOrderController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "Find order by Id", description = "Find an order by UUID", tags = {"orders"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/{id}"}, produces =  {"application/json"})
    public ResponseEntity<OrderResponse> findOrderById(
            @Parameter(description = "UUID of the order to be found. Cannot be empty.", example = "c9b31f33-17a4-4fcd-927e-c14cdee32201", required = true) @PathVariable("id") UUID id) {
        log.debug("API::Retrieving order with UUID: " + id);
        if (!productService.orderExistsById(id))
            throw new OrderNotFoundException("Order with UUID: " + id.toString() + " does not exist.");
        Optional<Order> order = productService.findOrderById(id);
        return order.map(value -> new ResponseEntity<>(new OrderResponse(value), HttpStatus.OK)).orElse(null);
    }

    @Operation(summary = "Find orders by keyword(s)", description = "Keyword search by %keyword% format", tags = {"products"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {""}, produces = {"application/json"})
    public ResponseEntity<List<OrderResponse>> getOrdersByKeywords(
            @Parameter(description = "Keyword(s) search for orders to be found.") @RequestParam("keywords") Optional<String> keywords,
            @Parameter(description = "Offset of the starting record. 0 indicates the first record.") @RequestParam("offset") Optional<Integer> offset,
            @Parameter(description = "Maximum records to return. The maximum value allowed is 50.") @RequestParam("limit") Optional<Integer> limit) {
        log.debug("API::Retrieving orders by keyword(s)");
        // TODO: implement keywords, offset and limit
        if (keywords.equals(Optional.empty())) {
            return ResponseEntity.ok().body(
                    productService.getAllOrders().stream()
                            .map(OrderResponse::new)
                            .collect(Collectors.toList()));
        } else {
            String k = (keywords.orElse(""));
            Integer o = (offset.orElse(0));
            return new ResponseEntity<>(
                    productService.getAllOrders(o, k).stream()
                            .map(OrderResponse::new)
                            .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    @Operation(summary = "Create a new order", description = "Creates a new order", tags = {"orders"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "409", description = "Order Already Exists", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PostMapping(value = {""}, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody OrderRequest newOrder) {
        log.debug("API::Creating new order: " + newOrder.toString());
        return new ResponseEntity<>(new OrderResponse(productService.saveOrderFromApi(null, newOrder)), HttpStatus.OK);
    }

    @Operation(summary = "Update an order", description = "Update an existing order", tags = {"orders"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @PutMapping(value = {"/{id}"}, produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<OrderResponse> updateOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "") @Valid @RequestBody OrderRequest newOrder,
            @Parameter(description = "UUID of the order to be updated. Cannot be empty.", example = "c9b31f33-17a4-4fcd-927e-c14cdee32201", required = true) @PathVariable("id") UUID id) {
        log.debug("API::Updating order with UUID: " + id);
        return new ResponseEntity<>(new OrderResponse(productService.saveOrderFromApi(id, newOrder)), HttpStatus.OK);
    }

    @Operation(summary = "Delete a order", description = "Delete an order", tags = {"orders"}, security = @SecurityRequirement(name = "JWT Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order Not Found", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class)))
    })
    @DeleteMapping (value = {"/{id}"})
    public ResponseEntity<ApiStatusResponse> deleteOrder(
            @Parameter(description = "UUID of the order to be updated. Cannot be empty.", example = "c9b31f33-17a4-4fcd-927e-c14cdee32201", required = true) @PathVariable("id") UUID id) {
        log.debug("API::Deleting order with UUID: " + id);
        productService.deleteOrderById(id);
        ApiStatusResponse apiStatusResponse = new ApiStatusResponse
                .ApiResponseBuilder()
                .withSuccess(true)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<>(apiStatusResponse, HttpStatus.OK);
    }

}


