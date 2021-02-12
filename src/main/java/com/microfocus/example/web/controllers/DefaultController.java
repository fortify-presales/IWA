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

import java.security.Principal;
import java.util.Currency;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Default (root) controllers
 *
 * @author Kevin A. Lee
 */
@SessionAttributes({"currentUser", "currentUserId"})
@Controller
public class DefaultController {

    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);

    @Value("${app.messages.home}")
    private String message = "Hello World";

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        Locale currentLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(currentLocale);
        model.addAttribute("currencySymbol", currency.getSymbol());
        model.addAttribute("message", message);
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, Principal principal) {
        String referer = request.getHeader("Referer");
        model.addAttribute("referer", referer);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, Principal principal) {
        return "register";
    }

    @GetMapping("/services")
    public String services(Model model, Principal principal) {
        return "services";
    }

    @GetMapping("/prescriptions")
    public String prescriptions(Model model, Principal principal) {
        return "prescriptions";
    }

    @GetMapping("/advice")
    public String advice(Model model, Principal principal) {
        return "advice";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model, Principal principal) {
        if (principal != null) {
        	log.debug("DefaultController:accessDenied: " + principal.toString());
            CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
            model.addAttribute("user", WebUtils.toString(loggedInUser.getUserDetails()));
            String message = "Sorry <strong>" + principal.getName() + "</strong> - " //
                    + "you do not have permission to access this page.";
            model.addAttribute("message", message);
        }
        return "/error/403-access-denied";
    }

    @GetMapping("/backdoor")
    public String backdoor(Model model, Principal principal) {
        log.debug("Oops! Someone has found the backdoor!");
        return "/admin/backdoor.html";
    }

    @GetMapping("/vulnerabilities")
    public String vulnerabilities(Model model, Principal principal) {
        return "/vulnerabilities.html";
    }

    @GetMapping("/not-yet-implemented")
    public String notYetImplemented(Model model, Principal principal) {
        return "/error/not-implemented.html";
    }

    @GetMapping("/site-message")
    @ResponseBody
    public String siteMessage() {
        return "This site is currently healthy.";
    }

}
