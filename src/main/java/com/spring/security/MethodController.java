package com.spring.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MethodController {

    @GetMapping("/")
    public Authentication index(Authentication authentication){
        return authentication;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin(){
        return "admin";
    }
    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String user(){
        return "user";
    }

    @GetMapping("/isAuthenticated")
    @PreAuthorize("isAuthenticated()")
    public String isAuthenticated(){
        return "isAuthenticated";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("#id == authentication.name")
    public String authentication(@PathVariable(name = "id") String id){
        return id;
    }

    @GetMapping("/owner")
    @PostAuthorize("returnObject.owner == authentication.name")
    public Account owner(String name, Principal principal){
        String name1 = principal.getName();
//        return new Account(name, true);
        return null;
    }

    @GetMapping("/isSecure")
    @PostAuthorize("hasAnyAuthority('ROLE_ADMIN') and returnObject.isSecure")
    public Account isSecure(String name, String secure){
//        return new Account(name, "Y".equals(secure));
        return null;

    }
}