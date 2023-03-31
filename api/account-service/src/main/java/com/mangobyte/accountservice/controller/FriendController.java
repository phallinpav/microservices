package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.dao.FriendRepository;
import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendRepository friendRepository;
    private final AccountService accountService;

    @GetMapping
    public List<Long> getAllFriends(ApiKeyAuthenticationToken auth) {
        Account account = auth.getAccount();
        List<Long> list = friendRepository.findFriendOfAccountId(account.getId());
        return list;
    }



}
