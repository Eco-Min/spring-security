package com.spring.security.users.service;

import com.spring.security.admin.repository.RoleRepository;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.Role;
import com.spring.security.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void createUser(Account account) {
//        Role role = roleRepository.findByRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        for (Role userRole : account.getUserRoles()) {
            Role role = roleRepository.findByRoleName(userRole.getRoleName());
            roles.add(role);
        }
        account.setUserRoles(roles);
        userRepository.save(account);
    }
}
