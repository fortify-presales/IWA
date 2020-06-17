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

package com.microfocus.example.web.controllers;

import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.BackupException;
import com.microfocus.example.exception.InvalidPasswordException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.service.ProductService;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.AdminUtils;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.BackupForm;
import com.microfocus.example.web.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for product pages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/products")
@Controller
public class WebProductController {

    private static final Logger log = LoggerFactory.getLogger(WebProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping(value = {"", "/"})
    public String index(Model model, @Param("keywords") String keywords, Principal principal) {
        this.setModelDefaults(model, principal, "Product", "index");
        List<Product> products = productService.listAll(keywords);
        model.addAttribute("keywords", keywords);
        model.addAttribute("products", products);
        model.addAttribute("productCount", products.size());
        model.addAttribute("productTotal", productService.count());
        return "products/index";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") Integer userId,
                           Model model, Principal principal) {
/*        Optional<User> optionalUser = userService.findById(userId);
        if (optionalUser.isPresent()) {
            UserForm userForm = new UserForm(optionalUser.get());
            model.addAttribute("userForm", userForm);
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }

 */
        this.setModelDefaults(model, principal, "Product", "view");
        return "products/view";
    }


    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }

}
