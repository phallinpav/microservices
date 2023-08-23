package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.exception.RecordNotFoundException;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
@RequiredArgsConstructor
public class BlockController {
    private final AccountService accountService;
    private final BlockService blockService;

    @PostMapping("/{id}")
    public void blockAccount(
            @PathVariable(name = "id") Long id,
            ApiKeyAuthenticationToken auth) {

        Account otherUserAcc = (accountService.findById(id)
                .orElseThrow(RecordNotFoundException::new));

        blockService.block(auth.getAccount(), otherUserAcc);
    }

}
