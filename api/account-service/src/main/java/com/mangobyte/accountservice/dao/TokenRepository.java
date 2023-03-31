package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    /*
        Will use this when refresh token implement:
        @Query("SELECT t.account FROM Token t WHERE t.token = ?1 AND t.expiredAt > NOW()")
     */
    @Query("SELECT t.account FROM Token t WHERE t.token = ?1")
    Optional<Account> findAccountByToken(String token);

    Optional<Token> findByAccountId(Long id);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.account.id = ?1")
    void deleteByAccountId(Long id);
}
