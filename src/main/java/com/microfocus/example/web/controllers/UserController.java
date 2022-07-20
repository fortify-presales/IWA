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
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.*;
import com.microfocus.example.service.StorageService;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for user pages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/user")
@Controller
@SessionAttributes("user")
public class UserController extends AbstractBaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String CONTROLLER_NAME = getClass().getName();

    @Value("${AUTHENTICATION_ERROR;Invalid authentication credentials were supplied.")
    private String AUTHENTICATION_ERROR;

    @Value("${USER_NOT_FOUND_ERROR:A user to be changed was not found.}")
    private String USER_NOT_FOUND_ERROR;

    @Value("${USERNAME_TAKEN_ERROR:A username was used that is already taken.")
    private String USERNAME_TAKEN_ERROR;

    @Value(("${EMAIL_ADDRESS_TAKEN_ERROR:An email address was used that is already taken.}"))
    private String EMAIL_ADDRESS_TAKEN_ERROR;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Value("${app.messages.home}")
    private final String message = "Hello World";

    @Autowired
    private SessionRegistry sessionRegistry;

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
            this.setModelDefaults(model, principal, "not-found");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "home");
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
            this.setModelDefaults(model, principal, "not-found");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "profile");
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
            this.setModelDefaults(model, principal, "not-found");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "edit-profile");
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
            this.setModelDefaults(model, principal, "not-found");
            return "user/not-found";
        }
        this.setModelDefaults(model, principal, "change-password");
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
        this.setModelDefaults(model, principal, "index");
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
            this.setModelDefaults(model, principal, "not-found");
            return "user/not-found";
        }
        Optional<Message> optionalMessage = userService.findMessageById(messageId);
        if (optionalMessage.isPresent()) {
            // does user have permission to read this message?
            UUID messageUserId = optionalMessage.get().getUser().getId();
            if (!messageUserId.equals(loggedInUserId)) {
                log.debug("User id: " + loggedInUserId + " trying to access message for: " + messageUserId);
                this.setModelDefaults(model, principal, "access-denied");
                return "user/messages/access-denied";
            }
            MessageForm messageForm = new MessageForm(optionalMessage.get());
            model.addAttribute("messageForm", messageForm);
            // mark messages as read
            userService.markMessageAsReadById(messageId);
        } else {
            model.addAttribute("message", "Internal error accessing message!");
            model.addAttribute("alertClass", "alert-danger");
            this.setModelDefaults(model, principal, "not-found");
            return "user/messages/not-found";
        }
        this.setModelDefaults(model, principal, "view");
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
        this.setModelDefaults(model, principal, "index");
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
            this.setModelDefaults(model, principal, "not-found");
            return "user/not-found";
        }
        Optional<Order> optionalOrder = userService.findOrderById(orderId);
        if (optionalOrder.isPresent()) {
            // does user have permission to view this order?
            UUID orderUserId = optionalOrder.get().getUser().getId();
            if (!orderUserId.equals(loggedInUserId)) {
                log.debug("User id: " + loggedInUserId + " trying to access order for: " + orderUserId);
                this.setModelDefaults(model, principal, "access-denied");
                return "user/orders/access-denied";
            }
            OrderForm orderForm = new OrderForm(optionalOrder.get());
            model.addAttribute("orderForm", orderForm);
        } else {
            model.addAttribute("message", "Internal error accessing order!");
            model.addAttribute("alertClass", "alert-danger");
            this.setModelDefaults(model, principal, "not-found");
            return "user/orders/not-found";
        }
        this.setModelDefaults(model, principal, "view");
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
            this.setModelDefaults(model, principal, "edit-profile");
            return "user/edit-profile";
        } else {
            try {
                userService.saveUserFromUserForm(userForm);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                this.setModelDefaults(model, principal, "profile");
                return "redirect:/user/profile";
            } catch (InvalidPasswordException ex) {
                log.error(AUTHENTICATION_ERROR);
                FieldError passwordError = new FieldError("userForm", "password", ex.getMessage());
                bindingResult.addError(passwordError);
            } catch (UserNotFoundException ex) {
                log.error(USER_NOT_FOUND_ERROR);
                FieldError usernameError = new FieldError("userForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            }
        }
        this.setModelDefaults(model, principal, "profile");
        return "user/profile";
    }

    @PostMapping("/savePassword")
    public String userSavePassword(@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm,
                                   BindingResult bindingResult, Model model,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        if (bindingResult.hasErrors()) {
            this.setModelDefaults(model, principal, "change-password");
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
                log.error(AUTHENTICATION_ERROR);
                FieldError passwordError = new FieldError("passwordForm", "password", ex.getMessage());
                bindingResult.addError(passwordError);
            } catch (UserNotFoundException ex) {
                log.error(USER_NOT_FOUND_ERROR);
                FieldError usernameError = new FieldError("passwordForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            }
        }
        this.setModelDefaults(model, principal, "home");
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
        this.setModelDefaults(model, principal, "register");
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerUserForm") RegisterUserForm registerUserForm,
                               BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            this.setModelDefaults(model, principal, "register");
            return "user/register";
        } else {
           try {
                User u = userService.registerUser(registerUserForm);
                log.debug("Created user '" + u.getEmail() + "' with validation token: " + u.getVerifyCode());
                this.setModelDefaults(model, null, "validate");
                return "redirect:/user/validate?email="+u.getEmail()+"&status=new";
            } catch (UsernameTakenException ex) {
                log.error(USERNAME_TAKEN_ERROR);
                FieldError usernameError = new FieldError("registerUserForm", "username", ex.getMessage());
                bindingResult.addError(usernameError);
            } catch (EmailAddressTakenException ex) {
               log.error(EMAIL_ADDRESS_TAKEN_ERROR);
               FieldError emailError = new FieldError("registerUserForm", "email", ex.getMessage());
               bindingResult.addError(emailError);
           }
        }
        this.setModelDefaults(model, principal, "register");
        return "user/register";
    }

    @GetMapping("/validate")
    public String validateUser(@RequestParam("email") Optional<String> usersEmail,
                               @RequestParam("code") Optional<String> validationCode,
                               @RequestParam("status") Optional<String> statusCode,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        String email = Optional.of(usersEmail).get().orElse(null);
        String code = Optional.of(validationCode).get().orElse(null);
        String status = Optional.of(statusCode).get().orElse(null);

        if (status != null && status.equals("new")) {
            model.addAttribute("message", "Your registration details have been stored. Please check your email to validate your details.");
            model.addAttribute("alertClass", "alert-success");
            ValidateUserForm validateUserForm = new ValidateUserForm(usersEmail, validationCode);
            model.addAttribute("validateUserForm", validateUserForm);
            this.setModelDefaults(model, null, "validate");
            return "user/validate";
        } else if ((email == null || email.isEmpty()) || (code == null || code.isEmpty())) {
            log.error("insufficient parameters");
            model.addAttribute("message", "You need to supply both an email address and validation code.");
            model.addAttribute("alertClass", "alert-danger");
            ValidateUserForm validateUserForm = new ValidateUserForm(usersEmail, validationCode);
            model.addAttribute("validateUserForm", validateUserForm);
            this.setModelDefaults(model, null, "validate");
            return "user/validate";
        } else {

            try {
                User u = userService.validateUserRegistration(email, code);
                if (u != null) {
                    log.debug("Successfully validated user '" + email + "'");
                    redirectAttributes.addFlashAttribute("message", "Your account has been successfully validated. Please login.");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                } else {
                    log.error("Unknown error validating user!");
                    redirectAttributes.addFlashAttribute("message", "There was an error validating your account, please contact support!");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                }
            } catch (UserNotFoundException ex) {
                log.error("Could not find user '" + email + "' to validate: " + ex.getLocalizedMessage());
                redirectAttributes.addFlashAttribute("message", "The account being validated does not exist. Please try registering again or contact support.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }

            return "redirect:/login";
        }

    }

    //
    // File uploads
    //

    @GetMapping("/uploadFile")
    public String listUploadedFiles(@Valid @ModelAttribute("uploadForm") UploadForm uploadForm,
                                    BindingResult bindingResult, Model model,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(UserController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "user/upload-file";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/user/upload-file";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
