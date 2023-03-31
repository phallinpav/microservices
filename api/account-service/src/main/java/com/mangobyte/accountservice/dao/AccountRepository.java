package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAll();

    Optional<Account> findById(Long id);

    Account save(Account account);

    Optional<Account> findFirstByUsername(String username);
}
