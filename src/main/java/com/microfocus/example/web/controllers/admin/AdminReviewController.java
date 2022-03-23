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

package com.microfocus.example.web.controllers.admin;

import com.microfocus.example.config.LocaleConfiguration;
import com.microfocus.example.entity.Review;
import com.microfocus.example.exception.ReviewNotFoundException;
import com.microfocus.example.service.ProductService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.admin.AdminReviewForm;
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
 * Controller for administration of product reviews
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/admin/reviews")
@Controller
public class AdminReviewController {

    private static final Logger log = LoggerFactory.getLogger(AdminReviewController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    LocaleConfiguration localeConfiguration;

    @GetMapping(value = {"", "/"})
    public String listReviews(Model model, Principal principal) {
        List<Review> reviews = productService.getReviews();
        model.addAttribute("reviews", reviews);
        this.setModelDefaults(model, principal, "Admin", "reviews");
        return "admin/reviews/index";
    }

    @GetMapping("/{id}")
    public String viewRevier(@PathVariable("id") UUID reviewId,
                               Model model, Principal principal) {
        Optional<Review> optionalReview = productService.findReviewById(reviewId);
        if (optionalReview.isPresent()) {
            AdminReviewForm adminReviewForm = new AdminReviewForm(optionalReview.get());
            model.addAttribute("adminReviewForm", adminReviewForm);
        } else {
            model.addAttribute("message", "Internal error accessing review!");
            model.addAttribute("alertClass", "alert-danger");
            return "review/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "viewReview");
        return "admin/reviews/view";
    }

    @GetMapping("/{id}/edit")
    public String editReview(@PathVariable("id") UUID reviewId,
                              Model model, Principal principal) {
        Optional<Review> optionalReview = productService.findReviewById(reviewId);
        if (optionalReview.isPresent()) {
            AdminReviewForm adminReviewForm = new AdminReviewForm(optionalReview.get());
            model.addAttribute("adminReviewForm", adminReviewForm);
        } else {
            model.addAttribute("message", "Internal error accessing review!");
            model.addAttribute("alertClass", "alert-danger");
            return "review/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "editReview");
        return "admin/reviews/edit";
    }

    @PostMapping("/{id}/save")
    public String saveReview(@Valid @ModelAttribute("adminReviewForm") AdminReviewForm adminReviewForm,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (!bindingResult.hasErrors()) {
            try {
                productService.saveReviewFromAdminReviewForm(adminReviewForm);
                redirectAttributes.addFlashAttribute("message", "Review updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/admin/reviews/" + adminReviewForm.getId();
            } catch (ReviewNotFoundException ex) {
                FieldError reviewIdError = new FieldError("adminReviewForm", "id", ex.getMessage());
                bindingResult.addError(reviewIdError);
            }
        }
        this.setModelDefaults(model, principal, "Admin", "saveReview");
        return "admin/reviews/edit";
    }

    @GetMapping("/{id}/delete")
    public String deleteReview(@PathVariable("id") UUID reviewId,
                             Model model, Principal principal) {
        Optional<Review> optionalReview = productService.findReviewById(reviewId);
        if (optionalReview.isPresent()) {
            AdminReviewForm adminReviewForm = new AdminReviewForm(optionalReview.get());
            model.addAttribute("adminReviewForm", adminReviewForm);
        } else {
            model.addAttribute("message", "Internal error accessing review!");
            model.addAttribute("alertClass", "alert-danger");
            return "review/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "deleteReview");
        return "admin/reviews/delete";
    }

    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable("id") UUID reviewId,
                             @RequestParam(value = "action", required = true) String action,
                             Model model, RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (action.equals("delete")) {
            productService.deleteReviewById(reviewId);
            redirectAttributes.addAttribute("message", "Review deleted successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            redirectAttributes.addAttribute("message", "Review deletion cancelled.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-info");
        }
        return "redirect:/admin/reviews/";
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
