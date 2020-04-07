/*
        Simple Secure App

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

import com.microfocus.example.entity.User;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.AdminUtils;
import com.microfocus.example.web.form.BackupForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Controller for administrative pages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/admin")
@Controller
public class WebAdminController {

    private static final Logger log = LoggerFactory.getLogger(WebAdminController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = {"", "/"})
    public String index(Model model, Principal principal) {
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", "Admin");
        model.addAttribute("actionName", "index");
        return "admin/index";
    }

    @GetMapping("/users")
    public String listUsers(Model model, Principal principal) {
        List<User> users = userService.findEnabledUsersByUsername(true, "");
        model.addAttribute("users", users);
        model.addAttribute("controllerName", "Admin");
        model.addAttribute("actionName", "users");
        return "admin/users";
    }

    @GetMapping("/backup")
    public String databaseBackup(Model model, Principal principal) {
        BackupForm backupForm = new BackupForm();
        int backupId = AdminUtils.getBackupId();
        backupForm.setId(backupId);
        backupForm.setStatus(AdminUtils.getDbStatus(backupId));
        model.addAttribute("backupForm", backupForm);
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", "Admin");
        model.addAttribute("actionName", "backup");
        return "admin/backup";
    }

    @GetMapping("/diagnostics")
    public String siteDiagnostics(Model model, Principal principal) {
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", "Admin");
        model.addAttribute("actionName", "diagnostics");
        return "admin/diagnostics";
    }

    @GetMapping("/messages")
    public String adminMessages(Model model, Principal principal) {
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", "Admin");
        model.addAttribute("actionName", "messages");
        return "admin/messages";
    }

    @PostMapping("/runDbBackup")
    public String runDbBackup(@Valid @ModelAttribute("backupForm") BackupForm backupForm,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (bindingResult.hasErrors()) {
            return "admin/backup";
        } else {
            log.info("Backup profile: " + backupForm.getProfile());
            int backUpId = 0;
            try {
                backUpId = AdminUtils.startDbBackup(backupForm.getProfile());
            } catch (Exception ignored) {
                log.error(ignored.getMessage());
            }
            log.info("Backup id: " + backUpId);
            redirectAttributes.addFlashAttribute("message", "Database backup started successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/admin";
        }
    }

    @PostMapping("/dumpUsers")
    public String dumpUsers(HttpServletRequest request, Model model,
                            @RequestParam(value = "usernames", required = false) String usernames,
                            @RequestParam(value = "status", required = false) boolean enabled) {
        List<User> users = userService.findEnabledUsersByUsername(enabled, usernames);
        model.addAttribute("users", users);
        model.addAttribute("controllerName", "Admin");
        model.addAttribute("actionName", "users");
        return "admin/users";
    }

}
