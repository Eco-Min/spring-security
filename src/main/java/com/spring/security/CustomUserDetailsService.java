package com.spring.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // username 은 ID 값으로 사용해야한다 -> 가끔씩 헷갈리는 경우가 있다.
        // 단독으로 사용보다는 provider 와 연계해서 사용한다.

        /*return User.withUsername("user")
                .password("{noop}1111")
                .roles("USER")
                .build();*/

        AccountDto accountDto = new AccountDto("user", "{noop}1111", List.of(new SimpleGrantedAuthority("USER")));
        return new CustomUserDetails(accountDto);
    }

}
