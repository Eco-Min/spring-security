package com.spring.security.users.repository;

import com.spring.security.users.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, String> {
    Account findByUsername(String username);
}
