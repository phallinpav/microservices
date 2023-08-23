package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.service.AccountsService;
import com.mangobyte.accountservice.view.AccountsView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountsService accountsService;

    @GetMapping("/search")
    public List<AccountsView> searchAccounts(ApiKeyAuthenticationToken auth,
                                             @RequestParam(name = "username", required = false) Optional<String> username) {
        return accountsService.search(auth.getAccount(), username);
    }
}
