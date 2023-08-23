package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Token;
import lombok.NonNull;

import java.util.Optional;

public interface TokenService {
    Token generate(@NonNull Token token);

    Optional<Account> findAccountByToken(@NonNull String token);
}
