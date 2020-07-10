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

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.InvalidPasswordException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.MessageForm;
import com.microfocus.example.web.form.PasswordForm;
import com.microfocus.example.web.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
            model.addAttribute("userForm", userForm);
            model.addAttribute("userInfo", WebUtils.toString(user.getUserDetails()));

        } else {
            model.addAttribute("message", "Internal error accessing user!");
            model.addAttribute("alertClass", "alert-danger");
            return "user/not-found";
        }
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", "User");
        model.addAttribute("actionName", "index");
        return "user/home";
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
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", "User");
        model.addAttribute("actionName", "editProfile");
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
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", "User");
        model.addAttribute("actionName", "changePassword");
        return "user/change-password";
    }

    @GetMapping("/messages")
    public String adminMessages(Model model, Principal principal) {
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        List<Message> messages = userService.getUserMessages(user.getId());
        model.addAttribute("messages", messages);
        model.addAttribute("unreadMessageCount", userService.getUserUnreadMessageCount(user.getId()));
        model.addAttribute("totalMessageCount", messages.size());
        model.addAttribute("controllerName", "User");
        model.addAttribute("actionName", "messages");
        return "user/messages/index";
    }

    @GetMapping("/unread-message-count")
    @ResponseBody
    public String getUserMessageCount(Model model, Principal principal) {
        if (principal != null) {
            CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
            long userMessageCount = userService.getUserUnreadMessageCount(loggedInUser.getId());
            return Long.toString(userMessageCount);
        } else {
            return "0";
        }
    }

    @GetMapping("/messages/{id}")
    public String viewMessage(@PathVariable("id") Integer messageId,
                           Model model, Principal principal) {
        Optional<Message> optionalMessage = userService.findMessageById(messageId);
        if (optionalMessage.isPresent()) {
            MessageForm messageForm = new MessageForm(optionalMessage.get());
            model.addAttribute("messageForm", messageForm);
            // mark messages as read
            userService.markMessageAsReadById(messageId);
        } else {
            model.addAttribute("message", "Internal error accessing message!");
            model.addAttribute("alertClass", "alert-danger");
            return "message/not-found";
        }
        return "/user/messages/view";
    }

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
                return "redirect:/user";
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
        return "user/edit-profile";
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
        return "user/index";
    }

    @PostMapping("/messages/delete/{id}")
    public String userDeleteMessage(@PathVariable("id") Integer messageId,
                              Model model, Principal principal) {
        userService.deleteMessageById(messageId);
        model.addAttribute("message", "Successfully deleted message!");
        model.addAttribute("alertClass", "alert-success");
        return "redirect:/user/messages/";
    }

}
