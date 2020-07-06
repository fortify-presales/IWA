package com.microfocus.example.web.controllers.admin;

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.Message;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

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
        this.setModelDefaults(model, principal, "Admin", "nessages");
        return "admin/messages/index";
    }

    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }
}
