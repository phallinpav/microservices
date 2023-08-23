package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.entity.Account;
import org.apache.commons.lang3.tuple.Pair;

public interface SendMailService {

    void sendConfirmationEmail(Account account);

    Pair<Long, String> decodeKey(String key);

}
