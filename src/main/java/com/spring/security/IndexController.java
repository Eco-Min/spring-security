package com.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/authentication")
    public Authentication index(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/user")
    public String user(){
        return "user";
    }

}
