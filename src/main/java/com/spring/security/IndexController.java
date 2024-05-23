package com.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private SecurityContextService securityContextService;

    @GetMapping("/")
    public String index() {
        securityContextService.securityContext();
        return "index";
    }

}