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

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Controller for user pages
 * @author Kevin A. Lee
 */
@RequestMapping("/user")
@Controller
public class WebUserController {

    private static final Logger log = LoggerFactory.getLogger(WebUserController.class);

    @Value("${messages.home:default-value}")
    private String message = "Hello World";

    @GetMapping(value = {"", "/"})
    public String userHome(Model model, Principal principal) {
        /*if (principal == null) {
            model.addAttribute("message", "Internal error accessing user");
            return "user/not-found";
        }
        CustomUserDetails user = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("userInfo", WebUtils.toString(user.getUserDetails()));*/
        model.addAttribute("controllerName", "User");
        model.addAttribute("actionName", "index");
        return "user/index";
    }

}
