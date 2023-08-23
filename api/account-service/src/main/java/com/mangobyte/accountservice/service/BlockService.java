package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.entity.Account;

public interface BlockService {
    void block(Account userAcc, Account otherUserAcc);

}
