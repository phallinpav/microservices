package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.form.AccountCreateForm;
import com.mangobyte.accountservice.form.AccountUpdateForm;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Value("${ui.server}")
    private String uiServer;

    @GetMapping
    public Account info(ApiKeyAuthenticationToken auth) {
        return auth.getAccount();
    }

    @PatchMapping
    public Account update(ApiKeyAuthenticationToken auth, @RequestBody @Valid AccountUpdateForm form) {
        return accountService.save(form.getUpdatedAccount(auth.getAccount()));
    }

    @PostMapping("/profileImg")
    public Account updateProfileImg(ApiKeyAuthenticationToken auth, @RequestPart MultipartFile image) {
        return accountService.updateProfileImg(auth.getAccount(), image);
    }

    @PostMapping
    public Account save(@RequestBody @Valid AccountCreateForm account) {
        return accountService.saveNeedVerify(account.toAccountUser());
    }

    @GetMapping("/verified")
    public RedirectView emailVerified(@RequestParam String key) {
        accountService.activateAccount(key);
        return new RedirectView(uiServer);
    }

    // FIXME: this concept of account active is not properly structure, need separate between active & email verified
    // TODO: need properly unit test for this email verified and future develop of account active or inactive
    // this is quick code update without good unit test. This was done while I was trying deploying code to AWS server.
}
