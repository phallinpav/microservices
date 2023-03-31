package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.Account;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAll();
    Optional<Account> findById(@NonNull Long id);
    Account save(@NonNull Account account);
    Optional<Account> findFirstByUsername(@NonNull String username);
}
