package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Block;
import com.mangobyte.accountservice.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void find_status_blocking_expect() {
        Block blocking = getStatusBlocking();

        Optional<Block> actual = blockRepository.findIsBlocked(blocking.getAccountFrom().getId(), blocking.getAccountTo().getId());

        assertTrue(actual.isPresent());
        assertEquals(actual.get(), blocking);
    }

    @Test
    void find_status_fail_blocking_expect() {
        Block blocking = getStatusUnblocked();

        Optional<Block> actual = blockRepository.findIsBlocked(blocking.getAccountFrom().getId(), blocking.getAccountTo().getId());

        assertFalse(actual.isPresent());
    }

    private Block getStatusBlocking() {
        Account account1 = SampleTestData.randomUserAccountRaw();
        Account account2 = SampleTestData.randomUserAccountRaw();

        accountRepository.save(account1);
        accountRepository.save(account2);

        Date now = CommonUtils.getDate();

        Block block = new Block();
        block.setAccountFrom(account1);
        block.setAccountTo(account2);
        block.setIsBlocked(true);
        block.setBlockedAt(now);

        return blockRepository.save(block);
    }

    private Block getStatusUnblocked() {
        Account account1 = SampleTestData.randomUserAccountRaw();
        Account account2 = SampleTestData.randomUserAccountRaw();

        accountRepository.save(account1);
        accountRepository.save(account2);

        Date now = CommonUtils.getDate();

        Block block = new Block();
        block.setAccountFrom(account1);
        block.setAccountTo(account2);
        block.setIsBlocked(false);
        block.setBlockedAt(now);

        return blockRepository.save(block);
    }
}
