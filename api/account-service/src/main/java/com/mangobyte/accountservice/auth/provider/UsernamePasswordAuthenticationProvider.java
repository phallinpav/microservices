package com.mangobyte.accountservice.auth.provider;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account account = accountService.findFirstByUsername(name)
                .orElseThrow(() -> new BadCredentialsException("invalid username or password"));

        if (!account.getActive()) {
            // FIXME: this concept of account active is not properly structure, need separate between active & email verified
            throw new BadCredentialsException("account email is not yet verified");
        }
        if (CommonUtils.isMatch(password, account.getPassword())) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(account.getRole().toString()));

            return new UsernamePasswordAuthenticationToken(account, password, authorities);
        } else {
            throw new BadCredentialsException("invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
