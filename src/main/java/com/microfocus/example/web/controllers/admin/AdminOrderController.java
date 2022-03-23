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

package com.microfocus.example.web.controllers.admin;

import com.microfocus.example.config.LocaleConfiguration;
import com.microfocus.example.entity.Order;
import com.microfocus.example.exception.OrderNotFoundException;
import com.microfocus.example.service.ProductService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.admin.AdminOrderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

/**
 * Controller for administration of orders
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/admin/orders")
@Controller
public class AdminOrderController {

    private static final Logger log = LoggerFactory.getLogger(AdminOrderController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    LocaleConfiguration localeConfiguration;

    @GetMapping(value = {"", "/"})
    public String listOrders(Model model, Principal principal) {
        List<Order> orders = productService.getAllOrders();
        model.addAttribute("orders", orders);
        this.setModelDefaults(model, principal, "Admin", "orders");
        return "admin/orders/index";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable("id") UUID orderId,
                               Model model, Principal principal) {
        Optional<Order> optionalOrder = productService.findOrderById(orderId);
        if (optionalOrder.isPresent()) {
            AdminOrderForm adminOrderForm = new AdminOrderForm(optionalOrder.get());
            model.addAttribute("adminOrderForm", adminOrderForm);
        } else {
            model.addAttribute("message", "Internal error accessing order!");
            model.addAttribute("alertClass", "alert-danger");
            return "order/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "viewOrder");
        return "admin/orders/view";
    }

    @GetMapping("/{id}/edit")
    public String editOrder(@PathVariable("id") UUID orderId,
                              Model model, Principal principal) {
        Optional<Order> optionalOrder = productService.findOrderById(orderId);
        if (optionalOrder.isPresent()) {
            AdminOrderForm adminOrderForm = new AdminOrderForm(optionalOrder.get());
            model.addAttribute("adminOrderForm", adminOrderForm);
        } else {
            model.addAttribute("message", "Internal error accessing order!");
            model.addAttribute("alertClass", "alert-danger");
            return "order/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "editOrder");
        return "admin/orders/edit";
    }

    @PostMapping("/{id}/save")
    public String saveOrder(@Valid @ModelAttribute("adminOrderForm") AdminOrderForm adminOrderForm,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (!bindingResult.hasErrors()) {
            try {
                productService.saveOrderFromAdminOrderForm(adminOrderForm);
                redirectAttributes.addFlashAttribute("message", "Order updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/admin/orders/" + adminOrderForm.getId();
            } catch (OrderNotFoundException ex) {
                FieldError orderIdError = new FieldError("adminOrderForm", "id", ex.getMessage());
                bindingResult.addError(orderIdError);
            }
        }
        this.setModelDefaults(model, principal, "Admin", "saveOrder");
        return "admin/orders/edit";
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") UUID orderId,
                             Model model, Principal principal) {
        Optional<Order> optionalOrder = productService.findOrderById(orderId);
        if (optionalOrder.isPresent()) {
            AdminOrderForm adminOrderForm = new AdminOrderForm(optionalOrder.get());
            model.addAttribute("adminOrderForm", adminOrderForm);
        } else {
            model.addAttribute("message", "Internal error accessing order!");
            model.addAttribute("alertClass", "alert-danger");
            return "order/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "deleteOrder");
        return "admin/orders/delete";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") UUID orderId,
                             @RequestParam(value = "action", required = true) String action,
                             Model model, RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (action.equals("delete")) {
            productService.deleteOrderById(orderId);
            redirectAttributes.addAttribute("message", "Order deleted successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            redirectAttributes.addAttribute("message", "Order deletion cancelled.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-info");
        }
        return "redirect:/admin/orders/";
    }

    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        Locale currentLocale = localeConfiguration.getLocale();
        Currency currency = Currency.getInstance(currentLocale);
        model.addAttribute("currencySymbol", currency.getSymbol());
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }

}
