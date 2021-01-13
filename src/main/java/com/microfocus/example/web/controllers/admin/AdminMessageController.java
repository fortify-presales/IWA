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

import com.microfocus.example.entity.Message;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.form.MessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for administration of messages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/admin/messages")
@Controller
public class AdminMessageController {

    private static final Logger log = LoggerFactory.getLogger(AdminMessageController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = {"", "/"})
    public String adminMessages(Model model, Principal principal) {
        List<Message> messages = userService.getAllMessages();
        model.addAttribute("messages", messages);
        this.setModelDefaults(model, principal, "Admin", "messages");
        return "admin/messages/index";
    }

    @GetMapping("/{id}")
    public String viewMessage(@PathVariable("id") UUID messageId,
                              Model model, Principal principal) {
        Optional<Message> optionalMessage = userService.findMessageById(messageId);
        if (optionalMessage.isPresent()) {
            MessageForm messageForm = new MessageForm(optionalMessage.get());
            model.addAttribute("messageForm", messageForm);
        } else {
            model.addAttribute("message", "Internal error accessing message!");
            model.addAttribute("alertClass", "alert-danger");
            return "message/not-found";
        }
        return "/admin/messages/view";
    }

    @PostMapping("/delete/{id}")
    public String userDeleteMessage(@PathVariable("id") UUID messageId,
                                    Model model, Principal principal) {
        userService.deleteMessageById(messageId);
        model.addAttribute("message", "Successfully deleted message!");
        model.addAttribute("alertClass", "alert-success");
        return "redirect:/admin/messages/";
    }

    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }
}
