package com.spring.security.users.controller;

import com.spring.security.users.domain.dto.AccountDto;
import com.spring.security.users.domain.entity.Account;
import com.spring.security.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder encoder;
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(AccountDto accountDto) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);
        account.setPassword(encoder.encode(account.getPassword()));

        userService.createUser(account);

        return "redirect:/";
    }
}
