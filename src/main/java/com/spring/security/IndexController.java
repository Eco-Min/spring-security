package com.spring.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("logoutSuccess")
    public String logoutSuccess() {
        return "logoutSuccess";
    }
}