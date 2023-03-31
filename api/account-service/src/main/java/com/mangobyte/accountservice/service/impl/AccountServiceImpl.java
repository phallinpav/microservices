package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.dao.AccountRepository;
import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.utils.CommonUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(@NonNull Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(@NonNull Account account) {
        String password = account.getPassword();
        Account createAccount = account.toBuilder()
                .password(CommonUtils.encodedPassword(password))
                .build();
        return accountRepository.save(createAccount);
    }

    @Override
    public Optional<Account> findFirstByUsername(@NonNull String name) {
        return accountRepository.findFirstByUsername(name);
    }

}
