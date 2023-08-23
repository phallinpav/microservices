package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.dao.FriendRepository;
import com.mangobyte.accountservice.exception.CustomException;
import com.mangobyte.accountservice.exception.RecordNotFoundException;
import com.mangobyte.accountservice.model.FriendStatus;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.service.FriendService;
import com.mangobyte.accountservice.utils.ErrorMessageUtils;
import com.mangobyte.accountservice.view.FriendRequestView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendRepository friendRepository;
    private final AccountService accountService;
    private final FriendService friendService;

    @GetMapping
    public List<Account> getAllFriends(ApiKeyAuthenticationToken auth) {
        Account authAccount = auth.getAccount();
        List<Account> list = friendRepository.findFriendOfAccountId(authAccount.getId());
        return list;
    }

    @GetMapping("/requests")
    public List<FriendRequestView> getListRequest(
            ApiKeyAuthenticationToken auth,
            @RequestParam(name = "fromMe", required = false, defaultValue = "false") Boolean fromMe) {
        return friendService.getListRequest(auth.getAccount().getId(), fromMe);
    }

    @PatchMapping("/request/{id}")
    public void responseRequest(
            @PathVariable(name = "id") Long id,
            @RequestBody Map<String, String> body,
            ApiKeyAuthenticationToken auth) {
        Account otherUserAcc = (accountService.findById(id))
                .orElseThrow(RecordNotFoundException::new);

        FriendStatus friendStatus;
        String status = Optional.ofNullable(body.get("status"))
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.INVALID_STATUS));

        try {
            friendStatus = FriendStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.INVALID_STATUS);
        }

        switch (friendStatus) {
            case ACCEPTED -> friendService.accept(auth.getAccount(), otherUserAcc);
            case DENIED -> friendService.deny(auth.getAccount(), otherUserAcc);
            case REMOVED -> friendService.unfriend(auth.getAccount(), otherUserAcc);
            default -> throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.INVALID_STATUS);
        }
    }

    @PostMapping("/add/{id}")
    public void add(@PathVariable Long id, ApiKeyAuthenticationToken auth) {
        Account otherUserAcc = (accountService.findById(id)
                .orElseThrow(RecordNotFoundException::new));
        friendService.add(auth.getAccount(), otherUserAcc);
    }

}
