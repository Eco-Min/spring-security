package com.spring.security.method;

import com.spring.security.Account;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.MethodInvocationResult;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

public class MyPostAuthorizationManager implements AuthorizationManager<MethodInvocationResult> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocationResult object) {
        Authentication auth = authentication.get();
        if (auth instanceof AnonymousAuthenticationToken) {
            return new AuthorizationDecision(false);
        }
        Account account = (Account) object.getResult();
        boolean isGranted = account.getOwner().equals(auth.getName());
        return new AuthorizationDecision(isGranted);
    }
}
