package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Token;
import com.mangobyte.accountservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/generate")
    public Token generate(UsernamePasswordAuthenticationToken auth) {
        Account account = (Account) auth.getPrincipal();
        Token token = new Token();
        token.setAccount(account);
        token.setToken(UUID.randomUUID().toString());
        token.setRefreshToken(UUID.randomUUID().toString());
        return tokenService.generate(token);
    }

}
