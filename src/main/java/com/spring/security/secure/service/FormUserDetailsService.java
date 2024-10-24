package com.spring.security.secure.service;

import com.spring.security.users.domain.dto.AccountContext;
import com.spring.security.users.domain.dto.AccountDto;
import com.spring.security.users.domain.entity.Account;
import com.spring.security.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username);
        if (account == null) {
                throw new UsernameNotFoundException("No user found with username: " + username);
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRoles()));
        ModelMapper modelMapper = new ModelMapper();
        AccountDto dto = modelMapper.map(account, AccountDto.class);
        return new AccountContext(dto, authorities);
    }
}
