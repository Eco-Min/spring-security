package com.spring.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DataService {
//    @PreAuthorize(value = "hasAnyAuthority('ROLE_USER')")
    @PreAuthorize(value = "") // annotation 은 필요 하지만 value 는 필요없음 -> proxy 등록
    public String getUser() {
        return "user";
    }

//    @PostAuthorize(value = "returnObject.owner == authentication.name")
    @PostAuthorize(value = "")
    public Account getOwner(String name) {
        return new Account(name, false);
    }

    public String display() {
        return "display";
    }
}