package com.iko.restapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/api")
public class HomeController {
    @GetMapping
    public String apiHome() {
        return "redirect:/swagger-ui/index.html";
    }
}
