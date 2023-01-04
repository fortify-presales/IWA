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

package com.microfocus.example.web.controllers.admin;

import com.microfocus.example.entity.User;
import com.microfocus.example.exception.BackupException;
import com.microfocus.example.exception.StorageFileNotFoundException;
import com.microfocus.example.service.StorageService;
import com.microfocus.example.service.UserService;
import com.microfocus.example.utils.AdminUtils;
import com.microfocus.example.utils.WebUtils;
import com.microfocus.example.web.controllers.UserController;
import com.microfocus.example.web.form.UploadForm;
import com.microfocus.example.web.form.admin.BackupForm;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for administrative pages
 *
 * @author Kevin A. Lee
 */
@RequestMapping("/admin")
@Controller
public class AdminDefaultController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String CONTROLLER_NAME = getClass().getName();

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    private String thRCECMD = "";

    @GetMapping(value = {"", "/"})
    public String index(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "Admin", "index");
        return "admin/dashboard";
    }

    @GetMapping("/backup")
    public String databaseBackup(Model model, Principal principal) {
        BackupForm backupForm = new BackupForm();
        int backupId = AdminUtils.getBackupId();
        backupForm.setId(backupId);
        backupForm.setStatus(AdminUtils.getDbStatus(backupId));
        model.addAttribute("backupForm", backupForm);
        this.setModelDefaults(model, principal, "Admin", "backup");
        return "admin/backup";
    }

    @GetMapping("/diagnostics")
    public String siteDiagnostics(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "Admin", "diagnostics");
        return "admin/diagnostics";
    }

    @PostMapping("/runDbBackup")
    public String runDbBackup(@Valid @ModelAttribute("backupForm") BackupForm backupForm,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (bindingResult.hasErrors()) {
            return "admin/backup";
        } else {
            log.debug("Backup profile: " + backupForm.getProfile());
            int backUpId = 0;
            try {
                backUpId = AdminUtils.startDbBackup(backupForm.getProfile());
            } catch (BackupException ignored) {
                log.error(ignored.getMessage());
            }
            log.debug("Backup id: " + backUpId);
            redirectAttributes.addFlashAttribute("message", "Database backup started successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            this.setModelDefaults(model, principal, "Admin", "index");
            return "redirect:/admin";
        }
    }

    @PostMapping("/dumpUsers")
    public String dumpUsers(HttpServletRequest request, Model model, Principal principal,
                            @RequestParam(value = "usernames", required = false) String usernames,
                            @RequestParam(value = "status", required = false) boolean enabled) {
        List<User> users = userService.findEnabledUsersByUsername(enabled, usernames);
        model.addAttribute("users", users);
        this.setModelDefaults(model, principal, "Admin", "users");
        return "admin/users";
    }

    @GetMapping("/command-shell")
    public String getCommandShell(Model model) {

        String cmdWrapper = "";
        if (Objects.nonNull(this.thRCECMD) && this.thRCECMD.length() > 2) {
            cmdWrapper = String.format("T    (java.lang.Runtime).getRuntime().exec('%s')", this.thRCECMD);
        }
        model.addAttribute("shellcmd", cmdWrapper);
        model.addAttribute("usercmd", this.thRCECMD);
        return "admin/command-shell";
    }

    @PostMapping("/command-shell")
    public String executeCommandShell(@RequestParam("cmdshell") String cmd,
                                      RedirectAttributes redirectAttributes) {

        this.thRCECMD = cmd;
        redirectAttributes.addFlashAttribute("message",
                "You successfully executed " + cmd + "!");
        return "redirect:/admin/command-shell";
    }

    @GetMapping("/log")
    public String ssrfExploit(Model model, @Param("val") String val) {
        int intVal = -1;
        String strLog = "";
        try {
            intVal = Integer.parseInt(val);
            strLog = "Input value is: "+intVal;
            log.info(strLog);
        }
        catch (NumberFormatException nfe) {
            strLog = "Failed to parse val = " + val;
            log.info("Failed to parse val = " + val);
        }

        model.addAttribute("val", val);
        model.addAttribute("intval", intVal);
        model.addAttribute("logwritten", strLog);

        return "admin/log";
    }

    private Model setModelDefaults(Model model, Principal principal, String controllerName, String actionName) {
        model.addAttribute("user", WebUtils.getLoggedInUser(principal));
        model.addAttribute("messageCount", "0");
        model.addAttribute("controllerName", controllerName);
        model.addAttribute("actionName", actionName);
        return model;
    }

}
