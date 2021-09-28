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

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.EmailAddressTakenException;
import com.microfocus.example.exception.InvalidPasswordException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.exception.UsernameTakenException;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
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
 * Controller for user pages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/user")
@Controller
@SessionAttributes("user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Value("${app.messages.home}")
    private final String message = "Hello World";

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping(value = {"", "/"})
    public String userHome(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Optional<User> optionalUser = userService.findUserById(user.getId());
        if (optionalUser.isPresent()) {
            UserForm userForm = new UserForm(optionalUser.get());
            model.addAttribute("username", userForm.getUsername());
            model.addAttribute("fullname", userForm.getFirstName() + " " + userForm.getLastName());
            model.addAttribute("userInfo", WebUtils.toString(user.getUserDetails()));
            model.addAttribute("unreadMessageCount", userService.getUserUnreadMessageCount(user.getId()));
            model.addAttribute("unshippedOrderCount", userService.getUserUnshippedOrderCount(user.getId()));
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "User", "home");
        return "user/home";
    }

    @GetMapping(value = {"/profile"})
    public String userProfile(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Optional<User> optionalUser = userService.findUserById(user.getId());
        if (optionalUser.isPresent()) {
            UserForm userForm = new UserForm(optionalUser.get());
            model.addAttribute("userForm", userForm);
            model.addAttribute("userInfo", WebUtils.toString(user.getUserDetails()));
            model.addAttribute("unreadMessageCount", userService.getUserUnreadMessageCount(user.getId()));
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "User", "profile");
        return "user/profile";
    }

    @GetMapping("/editProfile")
    public String userEditProfile(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Optional<User> optionalUser = userService.findUserById(user.getId());
        if (optionalUser.isPresent()) {
            UserForm userForm = new UserForm(optionalUser.get());
            model.addAttribute("userForm", userForm);
            model.addAttribute("userInfo", WebUtils.toString(user.getUserDetails()));
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "User", "editProfile");
        return "user/edit-profile";
    }

    @GetMapping("/changePassword")
    public String userChangePassword(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Optional<User> optionalUser = userService.findUserById(user.getId());
        if (optionalUser.isPresent()) {
            PasswordForm passwordForm = new PasswordForm(optionalUser.get());
            model.addAttribute("passwordForm", passwordForm);
            model.addAttribute("userInfo", WebUtils.toString(user.getUserDetails()));
        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "User", "changePassword");
        return "user/change-password";
    }

    //
    // Messages
    //

    @GetMapping("/messages")
    public String userMessages(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        List<Message> messages = userService.getUserMessages(user.getId());
        model.addAttribute("messages", messages);
        model.addAttribute("unreadMessageCount", userService.getUserUnreadMessageCount(user.getId()));
        model.addAttribute("totalMessageCount", messages.size());
        this.setModelDefaults(model, principal, "User", "messages");
        return "user/messages/index";
    }

    /*
    @GetMapping("/unread-message-count")
    @ResponseBody
    public String getUserMessageCount(Model model, Principal principal) {
        UUID loggedInUserId;
        if (principal != null) {
            CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
            loggedInUserId = loggedInUser.getId();
            long userMessageCount = userService.getUserUnreadMessageCount(loggedInUserId);
            return Long.toString(userMessageCount);
        } else {
            return "0";
        }
    }*/

    @GetMapping("/messages/{id}")
    public String viewMessage(@PathVariable("id") UUID messageId,
                           Model model, Principal principal) {
        UUID loggedInUserId;
        if (principal != null) {
            CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
            loggedInUserId = loggedInUser.getId();
        } else {
            return "user/not-found";
        }
        Optional<Message> optionalMessage = userService.findMessageById(messageId);
        if (optionalMessage.isPresent()) {
            // does user have permission to read this message?
            UUID messageUserId = optionalMessage.get().getUser().getId();
            if (!messageUserId.equals(loggedInUserId)) {
                log.debug("User id: " + loggedInUserId + " trying to access message for: " + messageUserId);
                return "user/messages/access-denied";
            }
            MessageForm messageForm = new MessageForm(optionalMessage.get());
            model.addAttribute("messageForm", messageForm);
            // mark messages as read
            userService.markMessageAsReadById(messageId);
        } else {
            model.addAttribute("message", "Internal error accessing message!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/messages/not-found";
        }
        this.setModelDefaults(model, principal, "User", "viewMessage");
        return "user/messages/view";
    }

    //
    // Orders
    //

    @GetMapping("/orders")
    public String userOrders(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        List<Order> orders = userService.getUserOrders(user.getId());
        model.addAttribute("orders", orders);
        model.addAttribute("unshippedOrderCount", userService.getUserUnshippedOrderCount(user.getId()));
        model.addAttribute("totalOrderCount", orders.size());
        this.setModelDefaults(model, principal, "User", "orders");
        return "user/orders/index";
    }

    @GetMapping("/unshipped-order-count")
    @ResponseBody
    public String getUnshippedOrderCount(Model model, Principal principal) {
        if (principal != null) {
            CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
            long unshippedOrderCount = userService.getUserUnshippedOrderCount(loggedInUser.getId());
            return Long.toString(unshippedOrderCount);
        } else {
            return "0";
        }
    }

    @GetMapping("/orders/{id}")
    public String viewOrder(@PathVariable("id") UUID orderId,
                              Model model, Principal principal) {
        UUID loggedInUserId;
        if (principal != null) {
            CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
            loggedInUserId = loggedInUser.getId();
        } else {
            return "user/not-found";
        }
        Optional<Order> optionalOrder = userService.findOrderById(orderId);
        if (optionalOrder.isPresent()) {
            // does user have permission to view this order?
            UUID orderUserId = optionalOrder.get().getUser().getId();
            if (!orderUserId.equals(loggedInUserId)) {
                log.debug("User id: " + loggedInUserId + " trying to access order for: " + orderUserId);
                return "user/orders/access-denied";
            }
            OrderForm orderForm = new OrderForm(optionalOrder.get());
            model.addAttribute("orderForm", orderForm);
            Locale currentLocale = Locale.getDefault();
            Currency currency = Currency.getInstance(currentLocale);
            model.addAttribute("locale", currentLocale);
            model.addAttribute("currencySymbol", currency.getSymbol());
        } else {
            model.addAttribute("message", "Internal error accessing order!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/orders/not-found";
        }
        this.setModelDefaults(model, principal, "User", "viewOrder");
        return "user/orders/view";
    }

    //
    //
    //

    @PostMapping("/saveProfile")
    public String userSaveProfile(@Valid @ModelAttribute("userForm") UserForm userForm,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        if (bindingResult.hasErrors()) {
            return "user/edit-profile";
        } else {
            try {
                userService.saveUserFromUserForm(userForm);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/user/profile";
            } catch (InvalidPasswordException ex) {
                log.error("InvalidPasswordException saving user profile: " + principal.toString());
                FieldError passwordError = new FieldError("userForm", "password", ex.getMessage());
                bindingResult.addError(passwordError);
            } catch (UserNotFoundException ex) {
                log.error("UserNotFoundException saving profile: " + principal.toString());
                FieldError usernameError = new FieldError("userForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            }
        }
        this.setModelDefaults(model, principal, "User", "saveProfile");
        return "user/profile";
    }

    @PostMapping("/savePassword")
    public String userSavePassword(@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm,
                                   BindingResult bindingResult, Model model,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        if (bindingResult.hasErrors()) {
            return "user/change-password";
        } else {
            try {
                CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
                Optional<User> optionalUser = userService.findUserById(user.getId());
                if (optionalUser.isPresent()) {
                    userService.updateUserPasswordFromPasswordForm(user.getId(), passwordForm);
                }
                redirectAttributes.addFlashAttribute("message", "Password updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/logout";
            } catch (InvalidPasswordException ex) {
                log.error("InvalidPasswordException saving user: " + principal.toString());
                FieldError passwordError = new FieldError("passwordForm", "password", ex.getMessage());
                bindingResult.addError(passwordError);
            } catch (UserNotFoundException ex) {
                log.error("UserNotFoundException saving user: " + principal.toString());
                FieldError usernameError = new FieldError("passwordForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            }
        }
        this.setModelDefaults(model, principal, "User", "savePassword");
        return "user/home";
    }

    @PostMapping("/messages/delete/{id}")
    public String userDeleteMessage(@PathVariable("id") UUID messageId,
                              Model model, Principal principal) {
        userService.deleteMessageById(messageId);
        model.addAttribute("message", "Successfully deleted message!");
        model.addAttribute("alertClass", "alert-success");
        return "redirect:/user/messages/";
    }

    @GetMapping("/register")
    public String registerUser(Model model, Principal principal) {
        RegisterUserForm registerUserForm = new RegisterUserForm();
        model.addAttribute("registerUserForm", registerUserForm);
        this.setModelDefaults(model, principal, "User", "register");
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerUserForm") RegisterUserForm registerUserForm,
                               BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "user/register";
        } else {
           try {
                User utmp = userService.registerUser(registerUserForm);
                return "redirect:/login?registerSuccess";
            } catch (UsernameTakenException ex) {
                log.error("UsernameTakenException registering user: " + registerUserForm.getUsername());
                FieldError usernameError = new FieldError("registerUserForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            } catch (EmailAddressTakenException ex) {
               log.error("EmailAddressTakenException registering user: " + registerUserForm.getEmail());
               FieldError emailError = new FieldError("registerUserForm", "email", ex.getMessage());
               bindingResult.addError(emailError);
           }
        }
        return "user/register";
    }

    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        Locale currentLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(currentLocale);
        model.addAttribute("currencySymbol", currency.getSymbol());
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }

}
