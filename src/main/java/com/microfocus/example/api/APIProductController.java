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

package com.microfocus.example.api;

import com.microfocus.example.entity.ApiErrorResponse;
import com.microfocus.example.entity.Product;
import com.microfocus.example.exception.ProductNotFoundException;
import com.microfocus.example.service.ProductService;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * A RESTFul controller for accessing product information.
 *
 * @author Kevin A. Lee
 */
@Api(description = "Retrieve, update, create and delete products.", tags = {"products"})
@RequestMapping(value = "/api/v1")
@RestController
public class APIProductController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(APIProductController.class);

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Find products", tags = { "products" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Product.class, responseContainer = "Iterable"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class)
    })
    @GetMapping(value = {"/products"}, produces = "application/json")
    public Iterable<Product> findProducts(
            @ApiParam("Keyword(s) search for products to be found.")
            @RequestParam("keywords") Optional<String> keywords,
            @ApiParam("Page number of products to start from.")
            @RequestParam("pageNum") Optional<Integer> pageNum) {
        if (keywords.equals(Optional.empty())) {
            log.debug("Retrieving all products");
            return productService.listAll();
        } else {
            Integer pNum = 1;
            if (!pageNum.equals(Optional.empty())) {
                pNum = pageNum.get();
            }
            log.debug("Retrieving products with keywords: " + keywords);
            return productService.listAll(pNum, keywords.get());
        }
    }

    @ApiOperation(value = "Find a specific product by its Id", tags = { "products" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Product.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Product not found", response = ApiErrorResponse.class),
    })
    @GetMapping(value = {"/product/{id}"}, produces = "application/json")
    public Optional<Product> byId(
            @ApiParam(name = "id",
                    value = "Id of the product to be found. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable("id") Integer id) {
        log.debug("Retrieving product id: " + id);
        if (!productService.existsById(id)) throw new ProductNotFoundException("Product with id: " + id.toString() + " does not exist.");
        return productService.findById(id);
    }

    @ApiOperation(value = "Create a new product", tags = { "products" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Product.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 409, message = "Product already exists", response = ApiErrorResponse.class)
    })
    @PostMapping(value = {"/products"}, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Product newProduct(
            @ApiParam("Details of the product to be created. Cannot be empty.")
            @Valid @RequestBody Product newProduct) {
        newProduct.setId(0); // set to 0 for sequence id generation
        log.debug("Creating new product: " + newProduct.toString());
        return productService.save(newProduct);
    }

    @ApiOperation(value = "Update an existing product", tags = { "products" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Product.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Product not found", response = ApiErrorResponse.class)
    })
    @PutMapping(value = {"/products/{id}"}, produces = "application/json")
    public Product updateProduct(
            @ApiParam("Details of the product to be updated. Cannot be empty.")
            @Valid @RequestBody Product newProduct,
            @ApiParam(name = "id",
                    value = "Id of the product to be updated. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Updating product id: " + id);
        return productService.save(newProduct);
    }

    @ApiOperation(value = "Delete a product", tags = { "products" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 404, message = "Product not found", response = ApiErrorResponse.class)
    })
    @DeleteMapping(value = "/products/{id}", produces = "application/json")
    void deleteEmployee(
            @ApiParam(name = "id",
                    value = "Id of the product to be deleted. Cannot be empty.",
                    example = "1",
                    required = true)
            @PathVariable Integer id) {
        log.debug("Deleting product id: " + id);
        productService.delete(id);
    }

}


