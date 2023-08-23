package com.mangobyte.accountservice.auth.provider;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();

        if (ObjectUtils.isEmpty(token)) {
            throw new InsufficientAuthenticationException("No API key in request");
        } else {
            Account account = tokenService.findAccountByToken(token)
                    .orElseThrow(() -> new BadCredentialsException("API Key is invalid"));
            if (!account.getActive()) {
                // FIXME: this concept of account active is not properly structure, need separate between active & email verified
                throw new BadCredentialsException("account is inactive");
            }
            return new ApiKeyAuthenticationToken(account, token);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
