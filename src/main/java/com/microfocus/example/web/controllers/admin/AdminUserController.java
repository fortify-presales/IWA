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

import com.microfocus.example.entity.User;
import com.microfocus.example.exception.InvalidPasswordException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.admin.AdminNewUserForm;
import com.microfocus.example.web.form.admin.AdminPasswordForm;
import com.microfocus.example.web.form.admin.AdminUserForm;
import com.microfocus.example.web.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for administration of users
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/admin/users")
@Controller
public class AdminUserController {

    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Value("${AUTHENTICATION_ERROR;Invalid authentication credentials were supplied.")
    private String AUTHENTICATION_ERROR;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"", "/"})
    public String listUsers(Model model, Principal principal) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        this.setModelDefaults(model, principal, "Admin", "users");
        return "admin/users/index";
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable("id") UUID userId,
                           Model model, Principal principal) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isPresent()) {
            UserForm userForm = new UserForm(optionalUser.get());
            model.addAttribute("userForm", userForm);
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "userView");
        return "admin/users/view";
    }

    @GetMapping("/{id}/edit")
    public String userEditProfile(@PathVariable("id") UUID userId,
                                  Model model, Principal principal) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isPresent()) {
            AdminUserForm adminUserForm = new AdminUserForm(optionalUser.get());
            model.addAttribute("adminUserForm", adminUserForm);
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "userEdit");
        return "admin/users/edit";
    }

    @PostMapping("/{id}/save")
    public String userSaveProfile(@Valid @ModelAttribute("adminUserForm") AdminUserForm adminUserForm,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.saveUserFromAdminUserForm(adminUserForm);
                redirectAttributes.addFlashAttribute("message", "User updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/admin/users/" + adminUserForm.getId();
            } catch (UserNotFoundException ex) {
                FieldError usernameError = new FieldError("adminUserForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            }
        }
        this.setModelDefaults(model, principal, "Admin", "userEdit");
        return "admin/users/edit";
    }

    @GetMapping("/{id}/changePassword")
    public String userChangePassword(@PathVariable("id") UUID userId,
                                     Model model, Principal principal) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isPresent()) {
            AdminPasswordForm adminPasswordForm = new AdminPasswordForm(optionalUser.get());
            model.addAttribute("adminPasswordForm", adminPasswordForm);
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "userChangePassword");
        return "admin/users/change-password";
    }

    @PostMapping("/{id}/savePassword")
    public String userSavePassword(@PathVariable("id") UUID userId,
                                   @Valid @ModelAttribute("adminPasswordForm") AdminPasswordForm adminPasswordForm,
                                   BindingResult bindingResult, Model model,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.updateUserPasswordFromAdminPasswordForm(userId, adminPasswordForm);
                redirectAttributes.addFlashAttribute("message", "User password updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/admin/users/" + userId;
            } catch (InvalidPasswordException ex) {
                log.error(AUTHENTICATION_ERROR);
                FieldError passwordError = new FieldError("adminPasswordForm", "password", ex.getMessage());
                bindingResult.addError(passwordError);
            } catch (UserNotFoundException ex) {
                FieldError usernameError = new FieldError("adminPasswordForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            }
        }
        this.setModelDefaults(model, principal, "Admin", "userEdit");
        return "/admin/users/change-password";
    }

    @GetMapping("/{id}/delete")
    public String userDelete(@PathVariable("id") UUID userId,
                             Model model, Principal principal) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isPresent()) {
            AdminUserForm adminUserForm = new AdminUserForm(optionalUser.get());
            model.addAttribute("adminUserForm", adminUserForm);
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "Admin", "userDelete");
        return "admin/users/delete";
    }

    @PostMapping("/{id}/delete")
    public String userDelete(@PathVariable("id") UUID userId,
                             @RequestParam(value = "action", required = true) String action,
                             Model model, RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (action.equals("delete")) {
            userService.deleteUserById(userId);
            redirectAttributes.addAttribute("message", "User deleted successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            redirectAttributes.addAttribute("message", "User deletion cancelled.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-info");
        }
        return "redirect:/admin/users/";
    }

    @GetMapping("/add")
    public String userAdd(Model model, Principal principal) {
        AdminNewUserForm adminNewUserForm = new AdminNewUserForm();
        model.addAttribute("adminNewUserForm", adminNewUserForm);
        this.setModelDefaults(model, principal, "Admin", "userAdd");
        return "admin/users/add";
    }

    @PostMapping("/addSave")
    public String userAddSave(@Valid @ModelAttribute("adminNewUserForm") AdminNewUserForm adminNewUserForm,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (!bindingResult.hasErrors()) {
            User u = userService.addUserFromAdminNewUserForm(adminNewUserForm);
            redirectAttributes.addFlashAttribute("message", "User " + adminNewUserForm.getUsername() + " added successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/admin/users/" + u.getId();
        }
        this.setModelDefaults(model, principal, "Admin", "userAddSave");
        return "admin/users/add";
    }

    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }

}
