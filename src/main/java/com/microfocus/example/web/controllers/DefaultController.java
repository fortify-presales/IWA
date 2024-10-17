/*
        Insecure Web App (IWA)

        Copyright (C) 2020-2024 Micro Focus or one of its affiliates

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
import com.microfocus.example.config.handlers.CustomAuthenticationSuccessHandler;
import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.MfaType;
import com.microfocus.example.entity.SMS;
import com.microfocus.example.exception.VerificationRequestFailedException;
import com.microfocus.example.payload.request.EmailRequest;
import com.microfocus.example.service.EmailSenderService;
import com.microfocus.example.service.SmsSenderService;
import com.microfocus.example.service.VerificationService;
import com.microfocus.example.utils.EmailUtils;
import com.microfocus.example.utils.JwtUtils;
import com.microfocus.example.utils.WebUtils;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

/**
 * Default (root) controllers
 *
 * @author kadraman
 */
@SessionAttributes({"currentUser", "currentUserId"})
@Controller
@Scope("session")
public class DefaultController extends AbstractBaseController{

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String CONTROLLER_NAME = getClass().getName();

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Value("${app.mail.from-name}")
    private String emailFromName;

    @Value("${app.mail.from-address}")
    private String emailFromAddress;

    @Value("${app.messages.home}")
    private String message = "Hello World";

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    LocaleConfiguration localeConfiguration;

    @Autowired
    VerificationService verificationService;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    SmsSenderService smsSenderService;

    @Override
    LocaleConfiguration GetLocaleConfiguration() {
        return localeConfiguration;
    }

    @Override
    String GetControllerName() {
        return CONTROLLER_NAME;
    }

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("message", message);
        this.setModelDefaults(model, principal, "index");
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, Principal principal) {
        HttpSession session = request.getSession(false);
        String referer = (String) request.getHeader("referer");
        session.setAttribute("loginReferer", referer);
        this.setModelDefaults(model, principal, "login");
        return "login";
    }

    @GetMapping("/login_mfa")
    public String otpLogin(HttpServletRequest request, Model model, Principal principal) {
        CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        String userId = loggedInUser.getId().toString();
        String email = loggedInUser.getEmail();
        String mobile = loggedInUser.getMobile();
        log.debug("Verifying user with id: " + userId);
        if (model.containsAttribute("otp")) {
            log.debug("OTP already set, revalidating");
        } else {
            try {
                // generate OTP "one-time-password" for user
                int otp = verificationService.generateOTP(userId);
                switch (loggedInUser.getMfaType()) {
                    case MFA_EMAIL:
                        log.debug("Generated OTP '" + String.valueOf(otp) + "' for user id: " + userId);
                        EmailRequest emailRequest = new EmailRequest(emailFromAddress, email, 
                            "[IWA Pharmacy Direct] Your One Time Passcode", String.valueOf(otp));
                        log.debug("Sending OTP {} via email to {}", String.valueOf(otp), email);
                        EmailUtils.sendEmail(emailRequest);
                        break;
                    case MFA_SMS:
                        SMS sms = new SMS();
                        sms.setTo(mobile);
                        sms.setMessage("Your IWA Pharmacy Direct security code is " + String.valueOf(otp));
                        log.debug("Sending OTP {} via SMS to {}", String.valueOf(otp), mobile);
                        String sid = smsSenderService.sendSms(sms);
                        break;
                    case MFA_APP:
                        log.debug("Using Authenticator App to validate TOTP");
                        break;
                    default:
                        log.error("Unknown MFA Type");
                }
            } catch (VerificationRequestFailedException ex) {
                log.error(ex.getLocalizedMessage());
                // TODO: handle
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage());
            }
        }
        return "login_mfa";
    }

    @PostMapping("/login_mfa")
    public String otpLogin(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("otp") Optional<String> otp,
                           Model model, Principal principal) {
        HttpSession session = request.getSession(true);
        Authentication authentication = (Authentication) principal;
        CustomUserDetails loggedInUser = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        String userId = loggedInUser.getId().toString();
        String otpStr = Optional.of(otp).get().orElse(null);

        try {
            int otpNum = Integer.valueOf(otpStr).intValue();
            // validate OTP "one-time-password" for user
            if (otpNum > 0) {
                log.debug("Verifying (T)OTP '{}' for user with id: {} ", otpNum, userId);
                if (loggedInUser.getMfaType().equals(MfaType.MFA_APP)) {
                    String secret = loggedInUser.getSecret();
                    log.debug("Validating TOTP {} with user secret {}", otpNum, secret);
                    GoogleAuthenticator gAuth = new com.warrenstrange.googleauth.GoogleAuthenticator();
                    if (gAuth.authorize(secret, otpNum)) {
                        log.debug("User '{}' verified TOTP successfully", userId);
                    } else {
                        log.debug("User '{}' failed TOTP verification", userId);
                        model.addAttribute("otpError", "true");
                        model.addAttribute("otpMessage", "Your OTP is incorrect, please try-again!");
                        return "login_mfa";
                    }
                } else {
                    int serverOtp = verificationService.getOtp(userId);
                    if (serverOtp > 0) {
                        if (otpNum == serverOtp) {
                            log.debug("User '{}' verified OTP successfully", userId);
                            verificationService.clearOTP(userId);
                        } else {
                            log.debug("User '{}' failed OTP verification", userId);
                            model.addAttribute("otpError", "true");
                            model.addAttribute("otpMessage", "Your OTP is incorrect, please try-again!");
                            return "login_mfa";
                        }
                    } else {
                        // TODO: fail
                    }
                }
            } else {
                // TODO: fail
            }
        } catch (NumberFormatException ex) {
            log.debug("User '{}' entered invalid otp/code", userId);
            model.addAttribute("otpError", "true");
            model.addAttribute("otpMessage", "The OTP must be a number");
            return "login_mfa";
        }
        String jwtToken = jwtUtils.generateAndSetSession(request, response, authentication);
        String targetUrl = CustomAuthenticationSuccessHandler.getTargetUrl(request, response, authentication);
        log.debug("Redirecting to {}", targetUrl);
        return "redirect:"+targetUrl;
    }

    @GetMapping("/services")
    public String services(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "services");
        return "services";
    }

    @GetMapping("/prescriptions")
    public String prescriptions(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "prescriptions");
        return "prescriptions";
    }

    @GetMapping("/advice")
    public String advice(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "advice");
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
        this.setModelDefaults(model, principal, "403-access-denied");
        return "error/403-access-denied";
    }

    @GetMapping("/backdoor")
    public String backdoor(Model model, Principal principal) {
        log.debug("Oops! Someone has found the backdoor!");
        this.setModelDefaults(model, principal, "backdoor");
        return "admin/backdoor";
    }

    @GetMapping("/not-yet-implemented")
    public String notYetImplemented(Model model, Principal principal) {
        this.setModelDefaults(model, principal, "not-implemented");
        return "error/not-implemented";
    }

    @GetMapping("/site-message")
    @ResponseBody
    public String siteMessage() {
        return "This site is currently healthy.";
    }

    @GetMapping("/backdoor/xss")
    @ResponseBody
    public ResponseEntity<String> getKeywordsContent(@Param("keywords") String keywords) {

    	String retContent = "Backdoor search using: " + keywords;

        return ResponseEntity.ok().body(retContent);
    }

}
