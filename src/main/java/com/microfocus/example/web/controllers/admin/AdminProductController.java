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
import com.microfocus.example.entity.Product;
import com.microfocus.example.exception.ProductNotFoundException;
import com.microfocus.example.service.ProductService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.admin.AdminNewProductForm;
import com.microfocus.example.web.form.admin.AdminProductForm;
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
 * Controller for administration of products
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/admin/products")
@Controller
public class AdminProductController {

    private static final Logger log = LoggerFactory.getLogger(AdminProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    LocaleConfiguration localeConfiguration;

    @GetMapping(value = {"", "/"})
    public String listProducts(Model model, Principal principal) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        this.setModelDefaults(model, principal, "Admin", "products");
        return "admin/products/index";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") UUID productId,
                              Model model, Principal principal) {
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (optionalProduct.isPresent()) {
            AdminProductForm adminProductForm = new AdminProductForm(optionalProduct.get());
            model.addAttribute("adminProductForm", adminProductForm);
        } else {
            model.addAttribute("message", "Internal error accessing product!");
            model.addAttribute("alertClass", "alert-danger");
            return "product/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "productView");
        return "admin/products/view";
    }

    @GetMapping("/{id}/edit")
    public String productEdit(@PathVariable("id") UUID productId,
                              Model model, Principal principal) {
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (optionalProduct.isPresent()) {
            AdminProductForm adminProductForm = new AdminProductForm(optionalProduct.get());
            model.addAttribute("adminProductForm", adminProductForm);
        } else {
            model.addAttribute("message", "Internal error accessing product!");
            model.addAttribute("alertClass", "alert-danger");
            return "product/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "productEdit");
        return "admin/products/edit";
    }

    @PostMapping("/{id}/save")
    public String productSave(@Valid @ModelAttribute("adminProductForm") AdminProductForm adminProductForm,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (!bindingResult.hasErrors()) {
            try {
                productService.saveProductFromAdminProductForm(adminProductForm);
                redirectAttributes.addFlashAttribute("message", "Product updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/admin/products/" + adminProductForm.getId();
            } catch (ProductNotFoundException ex) {
                FieldError productnameError = new FieldError("adminProductForm", "name", ex.getMessage());
                bindingResult.addError(productnameError);
            }
        }
        this.setModelDefaults(model, principal, "Admin", "productEdit");
        return "admin/products/edit";
    }

    @GetMapping("/{id}/delete")
    public String productDelete(@PathVariable("id") UUID productId,
                             Model model, Principal principal) {
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (optionalProduct.isPresent()) {
            AdminProductForm adminProductForm = new AdminProductForm(optionalProduct.get());
            model.addAttribute("adminProductForm", adminProductForm);
        } else {
            model.addAttribute("message", "Internal error accessing product!");
            model.addAttribute("alertClass", "alert-danger");
            return "product/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "productDelete");
        return "admin/products/delete";
    }

    @PostMapping("/{id}/delete")
    public String productDelete(@PathVariable("id") UUID productId,
                             @RequestParam(value = "action", required = true) String action,
                             Model model, RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (action.equals("delete")) {
            productService.deleteProductById(productId);
            redirectAttributes.addAttribute("message", "Product deleted successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            redirectAttributes.addAttribute("message", "Product deletion cancelled.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-info");
        }
        return "redirect:/admin/products/";
    }

    @GetMapping("/add")
    public String productAdd(Model model, Principal principal) {
        AdminNewProductForm adminNewProductForm = new AdminNewProductForm();
        model.addAttribute("adminNewProductForm", adminNewProductForm);
        this.setModelDefaults(model, principal, "Admin", "productAdd");
        return "admin/products/add";
    }

    @PostMapping("/addSave")
    public String productAddSave(@Valid @ModelAttribute("adminNewProductForm") AdminNewProductForm adminNewProductForm,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (!bindingResult.hasErrors()) {
            Product p = productService.newProductFormAdminNewProductForm(adminNewProductForm);
            redirectAttributes.addFlashAttribute("message", "Product '" + adminNewProductForm.getCode() + "' added successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/admin/products/" + p.getId();
        }
        this.setModelDefaults(model, principal, "Admin", "productAddSave");
        return "admin/products/add";
    }
    
    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        Locale currentLocale = localeConfiguration.getLocale();
        Currency currency = Currency.getInstance(currentLocale);
        model.addAttribute("currencySymbol", currency.getSymbol());
        model.addAttribute("product", WebUtils.getLoggedInUser(principal));
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }

}
