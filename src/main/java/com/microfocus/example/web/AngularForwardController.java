package com.microfocus.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AngularForwardController {

    @GetMapping("{path:^(?!api|public|assets|swagger)[^\\.]*}/**")
    public String handleForward() {
        return "forward:/";
    }

}
