package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.dao.AccountsRepository;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.AccountsService;
import com.mangobyte.accountservice.utils.ConvertUtils;
import com.mangobyte.accountservice.view.AccountsView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepository accountsRepository;

    @Override
    public List<AccountsView> search(Account userAcc, Optional<String> username) {

        Page<Account> accounts;
        Pageable paging = PageRequest.of(0, 10);
        if (username.isPresent()) {
            accounts = accountsRepository.getAccountsByUsername(userAcc.getId(), username.get(), paging);
        } else {
            accounts = accountsRepository.getAccounts(userAcc.getId(), paging);
        }
        return ConvertUtils.convertToAccountsView(accounts.getContent());

    }

}
