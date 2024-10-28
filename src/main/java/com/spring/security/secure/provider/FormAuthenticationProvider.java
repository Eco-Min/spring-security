package com.spring.security.secure.provider;

import com.spring.security.exception.SecretException;
import com.spring.security.secure.details.FormAuthenticationDetails;
import com.spring.security.users.domain.dto.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("authenticationProvider")
//@Component
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(loginId);

        if(!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("invalid password");
        }

        String secretKey = ((FormAuthenticationDetails) authentication.getDetails()).getSecretKey();
        if (secretKey == null || !secretKey.equals("secret")) {
            throw new SecretException("invalid secret");
        }

        return new UsernamePasswordAuthenticationToken(accountContext.getAccountDto(), null, accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) { // /api/login 호출시 RestAuthenticationToken.class 가 들어온다 token 값이 다르다 그래서 해당 관련된 로직을 다른 provider 를 만들어야 한다.
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
