package com.spring.security.secure.service;

import com.spring.security.secure.mapper.UrlRoleMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DynamicAuthorizationService {

    private final UrlRoleMapper delegate;

    public Map<String, String> getUrlRoleMappings() {
        return delegate.getUrlRoleMappings();
    }
}
