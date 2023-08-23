package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.entity.Account;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAll();

    Optional<Account> findById(@NonNull Long id);

    Account save(@NonNull Account account);

    Account saveNeedVerify(@NonNull Account account);

    Optional<Account> findFirstByUsername(@NonNull String username);

    void activateAccount(String key);

    Account updateProfileImg(@NonNull Account account, MultipartFile image);
}
