package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.view.AccountsView;

import java.util.List;
import java.util.Optional;

public interface AccountsService {
    List<AccountsView> search(Account userAcc, Optional<String> username);
}
