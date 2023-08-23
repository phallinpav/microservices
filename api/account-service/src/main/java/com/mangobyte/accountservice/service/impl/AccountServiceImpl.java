package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.dao.AccountRepository;
import com.mangobyte.accountservice.exception.RecordNotFoundException;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.service.FileProxyService;
import com.mangobyte.accountservice.service.SendMailService;
import com.mangobyte.accountservice.utils.CommonUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final SendMailService sendMailService;

    private final FileProxyService fileProxyService;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(@NonNull Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(@NonNull Account account) {
        String password = account.getPassword();
        Account createAccount = account.toBuilder()
                .password(CommonUtils.encodedPassword(password))
                .active(true)
                .build();
        return accountRepository.save(createAccount);
    }

    @Override
    public Account saveNeedVerify(@NonNull Account account) {
        String password = account.getPassword();
        Account createAccount = account.toBuilder()
                .password(CommonUtils.encodedPassword(password))
                .active(false)
                .build();
        accountRepository.save(createAccount);
        sendMailService.sendConfirmationEmail(createAccount);
        return createAccount;
    }

    @Override
    public Optional<Account> findFirstByUsername(@NonNull String name) {
        return accountRepository.findFirstByUsername(name);
    }

    @Override
    public void activateAccount(String key) {
        Pair<Long, String> pair = sendMailService.decodeKey(key);
        Long id = pair.getLeft();
        String email = pair.getRight();
        if (id != 0) {
            Account account = accountRepository.findFirstByIdAndEmail(id, email).orElseThrow(RecordNotFoundException::new);
            account.setActive(true);
            accountRepository.save(account);
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public Account updateProfileImg(@NonNull Account account, MultipartFile image) {
        String profileImgUrl = fileProxyService.uploadProfileImg(account.getId(), image);
        Account updatedAcc = account.toBuilder().profileImgUrl(profileImgUrl).build();
        accountRepository.save(updatedAcc);
        return updatedAcc;
    }
}
