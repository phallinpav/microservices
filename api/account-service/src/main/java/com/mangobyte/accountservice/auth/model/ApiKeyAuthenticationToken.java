package com.mangobyte.accountservice.auth.model;

import com.mangobyte.accountservice.model.entity.Account;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.AuthorityUtils;

@Transient
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    @Getter
    private Account account;
    private String token;

    public ApiKeyAuthenticationToken(Account account, String token) {
        super(AuthorityUtils.createAuthorityList(account.getRole().toString()));
        this.account = account;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }
}
