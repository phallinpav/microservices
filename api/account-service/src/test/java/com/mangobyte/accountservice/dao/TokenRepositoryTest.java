package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.model.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void saveToken_duplicated() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        accountRepository.save(acc1);

        Token token = SampleTestData.randomTokenRaw(acc1);
        tokenRepository.save(token);

        Token token2 = SampleTestData.randomTokenRaw(acc1);
        assertThrows(DataIntegrityViolationException.class, () -> tokenRepository.save(token2));
    }

    @Test
    void saveToken_accNotExist() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        Token token = SampleTestData.randomTokenRaw(acc1);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> tokenRepository.save(token));
    }

    @Test
    void findByAccountId() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        accountRepository.save(acc1);

        Token token = SampleTestData.randomTokenRaw(acc1);
        tokenRepository.save(token);

        Optional<Token> resToken = tokenRepository.findByAccountId(acc1.getId());
        Optional<Token> resTokenEmpty = tokenRepository.findByAccountId(100L);

        assertFalse(resTokenEmpty.isPresent());
        assertTrue(resToken.isPresent());
        assertEquals(resToken.get(), token);
    }

    @Test
    void findAccountByToken() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        accountRepository.save(acc1);

        Token token = SampleTestData.randomTokenRaw(acc1);
        tokenRepository.save(token);

        Optional<Account> resAcc = tokenRepository.findAccountByToken(token.getToken());
        Optional<Account> resAccEmpty = tokenRepository.findAccountByToken(UUID.randomUUID().toString());

        assertTrue(resAcc.isPresent());
        assertFalse(resAccEmpty.isPresent());
        assertEquals(resAcc.get(), acc1);
    }
}
