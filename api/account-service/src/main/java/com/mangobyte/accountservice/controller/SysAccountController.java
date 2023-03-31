package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.exception.RecordNotFoundException;
import com.mangobyte.accountservice.form.AccountCreateForm;
import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sys/accounts")
@RequiredArgsConstructor
public class SysAccountController {
    private final AccountService accountService;

    @GetMapping
    public List<Account> list() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable Long id) {
        return accountService.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    @PostMapping
    public Account save(@RequestBody @Valid AccountCreateForm account) {
        return accountService.save(account.toAccountUser());
    }
}
