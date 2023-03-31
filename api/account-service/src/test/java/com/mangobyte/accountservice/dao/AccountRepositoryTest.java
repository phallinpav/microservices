package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.model.Account;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Test
    void save_duplicated() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        Account acc1_1 = acc1.toBuilder().build();
        Account acc2 = SampleTestData.randomUserAccountRaw().toBuilder()
                .username(acc1.getUsername())
                .build();

        Account acc3 = SampleTestData.randomUserAccountRaw().toBuilder()
                .email(acc1.getEmail())
                .build();

        repository.save(acc1);

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(acc1_1));
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(acc2));
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(acc3));
    }

    @Test
    void save_update() {
        Account accOriginal = SampleTestData.randomUserAccountRaw();
        Account accRandom = SampleTestData.randomUserAccountRaw();

        repository.save(accOriginal);

        Account accUpdate = accOriginal.toBuilder()
                .username(accRandom.getUsername())
                .email(accRandom.getEmail())
                .password(accRandom.getPassword())
                .build();

        repository.save(accUpdate);
        Optional<Account> accUpdated = repository.findById(accOriginal.getId());

        assertTrue(accUpdated.isPresent());
        assertEquals(accUpdated.get(), accUpdate);
    }

    @Test
    void save_findAccountById() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        Account acc2 = SampleTestData.randomUserAccountRaw();

        repository.save(acc1);
        repository.save(acc2);

        Optional<Account> resAcc1 = repository.findById(acc1.getId());
        Optional<Account> resAcc2 = repository.findById(acc2.getId());
        Optional<Account> resAccEmpty = repository.findById(100L);

        assertTrue(resAcc1.isPresent());
        assertTrue(resAcc2.isPresent());
        assertFalse(resAccEmpty.isPresent());

        assertEquals(resAcc1.get(), acc1);
        assertEquals(resAcc2.get(), acc2);
    }

    @Test
    void save_findAll() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        Account acc2 = SampleTestData.randomUserAccountRaw();
        Account acc3 = SampleTestData.randomUserAccountRaw();

        repository.save(acc1);
        repository.save(acc2);
        repository.save(acc3);

        assertThat(repository.findAll())
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(Arrays.asList(SampleTestData.ADMIN_ACCOUNT, acc1, acc2, acc3));
    }

    @Test
    void save_findFirstByUsername() {
        Account acc1 = SampleTestData.randomUserAccountRaw();
        Account acc2 = SampleTestData.randomUserAccountRaw();

        repository.save(acc1);
        repository.save(acc2);

        Optional<Account> resAcc1 = repository.findFirstByUsername(acc1.getUsername());
        Optional<Account> resAcc2 = repository.findFirstByUsername(acc2.getUsername());
        Optional<Account> resAccEmpty = repository.findFirstByUsername(RandomStringUtils.randomAlphanumeric(3, 30));

        assertTrue(resAcc1.isPresent());
        assertTrue(resAcc2.isPresent());
        assertFalse(resAccEmpty.isPresent());

        assertEquals(resAcc1.get(), acc1);
        assertEquals(resAcc2.get(), acc2);
    }
}
