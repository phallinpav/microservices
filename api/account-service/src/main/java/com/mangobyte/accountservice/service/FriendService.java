package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.view.FriendRequestView;

import java.util.List;

public interface FriendService {
    void add(Account userAcc, Account otherUserAcc);

    List<FriendRequestView> getListRequest(Long userAccId, Boolean fromMe);

    void deny(Account userAcc, Account otherUserAcc);

    void accept(Account userAcc, Account otherUserAcc);

    void unfriend(Account userAcc, Account otherUserAcc);
}
