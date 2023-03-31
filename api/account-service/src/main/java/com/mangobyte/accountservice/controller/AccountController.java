package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.form.AccountUpdateForm;
import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping()
    public Account info(ApiKeyAuthenticationToken auth) {
        return auth.getAccount();
    }

    @PatchMapping()
    public Account update(ApiKeyAuthenticationToken auth, @RequestBody @Valid AccountUpdateForm form) {
        return accountService.save(form.getUpdatedAccount(auth.getAccount()));
    }
}
