/*
        Insecure Web App (IWA)

        Copyright (C) 2020-2022 Micro Focus or one of its affiliates

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

import com.microfocus.example.config.LocaleConfiguration;
import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.service.ProductService;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.OrderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

/**
 * Controller for shopping cart pages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/cart")
@Controller
public class CartController extends AbstractBaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String CONTROLLER_NAME = getClass().getName();

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    LocaleConfiguration localeConfiguration;

    @Override
    LocaleConfiguration GetLocaleConfiguration() {
        return localeConfiguration;
    }

    @Override
    String GetControllerName() {
        return CONTROLLER_NAME;
    }

    @GetMapping(value = {"", "/"})
    public String index(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "index");
        return "cart/index";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Optional<User> optionalUser = userService.findUserById(user.getId());
        if (optionalUser.isPresent()) {
            User utmp = optionalUser.get();
            OrderForm orderForm = new OrderForm();
            orderForm.setUser(utmp);
            model.addAttribute("orderForm", orderForm);
            model.addAttribute("userInfo", WebUtils.toString(user.getUserDetails()));
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            this.setModelDefaults(model, principal, "not-found");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "checkout");
        return "cart/checkout";
    }

    @PostMapping("/order")
    public String cartOrder(@Valid @ModelAttribute("orderForm") OrderForm orderForm,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        if (bindingResult.hasErrors()) {
            this.setModelDefaults(model, principal, "confirm");
            return "cart/confirm";
        } else {
            try {
                //userService.saveUserFromUserForm(userForm);
                Order otmp = productService.newOrderFromOrderForm(orderForm);
                redirectAttributes.addFlashAttribute("orderNum", otmp.getOrderNum());
                redirectAttributes.addFlashAttribute("orderId", otmp.getId());
                this.setModelDefaults(model, principal, "confirm");
                return "redirect:/cart/confirm";
            } catch (UserNotFoundException ex) {
                log.error("UserNotFoundException saving profile: " + principal.toString());
                //FieldError usernameError = new FieldError("userForm", "username", ex.getMessage());
                //bindingResult.addError(usernameError);
            }
        }
        this.setModelDefaults(model, principal, "confirm");
        return "cart/confirm";
    }

    @GetMapping("/confirm")
    public String order(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "confirm");
        return "cart/confirm";
    }

}
