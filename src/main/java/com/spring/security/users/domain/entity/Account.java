package com.spring.security.users.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private int age;
    private String roles;

}
