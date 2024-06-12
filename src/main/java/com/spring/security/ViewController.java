package com.spring.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

}
