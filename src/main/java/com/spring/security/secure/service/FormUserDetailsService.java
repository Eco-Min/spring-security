package com.spring.security.secure.service;

import com.spring.security.domain.dto.AccountContext;
import com.spring.security.domain.dto.AccountDto;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.Role;
import com.spring.security.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
//@Service
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username);
        if (account == null) {
                throw new UsernameNotFoundException("No user found with username: " + username);
        }

//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRoles()));
        List<GrantedAuthority> authorities = account.getUserRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet())
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        ModelMapper modelMapper = new ModelMapper();
        AccountDto dto = modelMapper.map(account, AccountDto.class);
        return new AccountContext(dto, authorities);
    }
}
