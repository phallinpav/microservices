package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.dao.TokenRepository;
import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.model.Role;
import com.mangobyte.accountservice.model.Token;
import com.mangobyte.accountservice.service.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Token generate(@NonNull Token token) {
        Optional<Token> rsToken = tokenRepository.findByAccountId(token.getAccount().getId());
        if (rsToken.isPresent()) {
            if (rsToken.get().getAccount().getRole().equals(Role.ADMIN)) {
                return rsToken.get();
            } else {
                tokenRepository.deleteByAccountId(rsToken.get().getAccount().getId());
            }
        }
        return tokenRepository.save(token);
    }

    @Override
    public Optional<Account> findAccountByToken(@NonNull String token) {
        return tokenRepository.findAccountByToken(token);
    }
}
