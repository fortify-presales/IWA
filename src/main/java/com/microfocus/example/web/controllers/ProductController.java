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

package com.microfocus.example.web.controllers;

import com.microfocus.example.entity.Product;
import com.microfocus.example.service.ProductService;
import com.microfocus.example.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.*;

/**
 * Controller for product pages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/products")
@Controller
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Value("${app.data.page-size:25}")
    private Integer defaultPageSize;

    @Autowired
    private ProductService productService;

    @GetMapping(value = {"", "/"})
    public String index(Model model, @Param("keywords") String keywords, @Param("limit") Integer limit, Principal principal) {
        log.debug("keywords="+keywords);
        this.setModelDefaults(model, principal, "Product", "index");
        if (limit == null) limit = defaultPageSize;
        productService.setPageSize(limit);
        List<Product> products = productService.getAllActiveProducts(0, keywords);
        model.addAttribute("keywords", keywords);
        model.addAttribute("products", products);
        model.addAttribute("productCount", products.size());
        model.addAttribute("productTotal", productService.count());
        return "products/index";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") UUID productId,
                              Model model, Principal principal) {
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
        } else {
            model.addAttribute("message", "Internal error accessing product!");
            model.addAttribute("alertClass", "alert-danger");
            return "products/not-found";
        }
        this.setModelDefaults(model, principal, "Product", "view");
        return "products/view";
    }

    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        Locale currentLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(currentLocale);
        model.addAttribute("currencySymbol", currency.getSymbol());
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }

}
