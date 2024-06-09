package com.spring.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public Authentication index(Authentication authentication) {
        return authentication;
    }

    @PostMapping("/csrf")
    public String csrf(){
        return "csrf 적용 됨";
    }

    @GetMapping("/csrfToken")
    public String csrfToken(HttpServletRequest request) {

        CsrfToken csrfToken1 = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        CsrfToken csrfToken2 = (CsrfToken) request.getAttribute("_csrf");

        String token = csrfToken1.getToken();
        System.out.println("token = " + token);
        System.out.println("csrfToken2 = " + csrfToken2.getToken());

        return token;
    }

}
