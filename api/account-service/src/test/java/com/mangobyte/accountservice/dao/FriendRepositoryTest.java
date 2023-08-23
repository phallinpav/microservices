package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.model.FriendStatus;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Friend;
import com.mangobyte.accountservice.utils.CommonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FriendRepositoryTest {

    @Autowired
    private FriendRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void save_status_update_accepted() {
        Friend requesting = getFriendStatusRequesting();
        Friend accepted = getFriendStatusAccepted();

        repository.save(requesting);

        Friend requestUpdate = requesting.toBuilder()
                .status(accepted.getStatus())
                .requestedAt(CommonUtils.getDate())
                .build();

        repository.save(requestUpdate);
        Optional<Friend> requestAccepted = repository.findById(requesting.getId());

        assertTrue(requestAccepted.isPresent());
        assertEquals(requestAccepted.get(), requestUpdate);
    }

    @Test
    void find_account_requesting() {

        Friend requesting = getFriendStatusRequesting();

        Optional<Friend> result = repository.findAcceptedOrRequesting(requesting.getAccountFrom().getId(), requesting.getAccountTo().getId());

        assertTrue(result.isPresent());
        assertEquals(result.get(), requesting);
    }

    @Test
    void find_account_accepted() {
        Friend accepted = getFriendStatusAccepted();
        Optional<Friend> result = repository.findAcceptedOrRequesting(accepted.getAccountFrom().getId(), accepted.getAccountTo().getId());

        assertTrue(result.isPresent());
        assertEquals(result.get(), accepted);
    }

    @Test
    void find_specific_requesting() {
        Friend requesting = getFriendStatusRequesting();

        Optional<Friend> result = repository.findPendingRequest(requesting.getAccountFrom().getId(), requesting.getAccountTo().getId());

        assertTrue(result.isPresent());
        assertEquals(result.get(), requesting);
    }

    @Test
    void find_requesting_from_admin_to_user_fail() {
        Friend accepted = getFriendStatusAccepted();

        Optional<Friend> result = repository.findPendingRequest(accepted.getAccountFrom().getId(), accepted.getAccountTo().getId());

        assertFalse(result.isPresent());
    }

    @Test
    void find_friendStatus_accepted() {
        Friend accepted = getFriendStatusAccepted();

        Optional<Friend> result = repository.findAccepted(accepted.getAccountFrom().getId(), accepted.getAccountTo().getId());

        assertTrue(result.isPresent());
        assertEquals(result.get(), accepted);
    }

    @Test
    void find_friendStatus_accepted_fail() {
        Friend requesting = getFriendStatusRequesting();

        Optional<Friend> result = repository.findAccepted(requesting.getAccountFrom().getId(), requesting.getAccountTo().getId());

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("find a data account_from = acc1 and status requesting")
    void save_find_status_requesting_from_auth() {
        Long acc1Id = saveAccountAndFriendDataAndGetAcc1Id();
        List<Friend> list = repository.findRequestingFrom(acc1Id);
        for (Friend actual : list) {
            assertEquals(acc1Id, actual.getAccountFrom().getId());
            assertEquals(FriendStatus.REQUESTING, actual.getStatus());
            break;
        }
    }


    @Test
    @DisplayName("find a data account_to = acc1 and status requesting")
    void save_find_status_requesting_to_auth() {
        Long acc1Id = saveAccountAndFriendDataAndGetAcc1Id();
        List<Friend> list = repository.findRequestingTo(acc1Id);
        for (Friend actual : list) {
            assertEquals(acc1Id, actual.getAccountTo().getId());
            assertEquals(FriendStatus.REQUESTING, actual.getStatus());
            break;
        }
    }

    private Long saveAccountAndFriendDataAndGetAcc1Id() {
        final Date now = CommonUtils.getDate();
        Account acc1 = SampleTestData.randomUserAccountRaw();
        Account acc2 = SampleTestData.randomUserAccountRaw();
        Account acc3 = SampleTestData.randomUserAccountRaw();

        accountRepository.saveAll(Arrays.asList(acc1, acc2, acc3));

        Friend friend1 = new Friend();
        friend1.setAccountFrom(acc1);
        friend1.setAccountTo(acc2);
        friend1.setStatus(FriendStatus.REQUESTING);
        friend1.setRequestedAt(now);

        Friend friend2 = new Friend();
        friend2.setAccountFrom(acc1);
        friend2.setAccountTo(acc3);
        friend2.setStatus(FriendStatus.ACCEPTED);
        friend2.setRequestedAt(now);

        repository.save(friend1);
        repository.save(friend2);
        return acc1.getId();
    }

    private Friend getFriendStatusRequesting() {
        final Date now = CommonUtils.getDate();
        Account account1 = SampleTestData.randomUserAccountRaw();
        Account account2 = SampleTestData.randomUserAccountRaw();

        accountRepository.save(account1);
        accountRepository.save(account2);

        Friend friendRequesting = new Friend().toBuilder()
                .accountFrom(account1)
                .accountTo(account2)
                .status(FriendStatus.REQUESTING)
                .requestedAt(now)
                .build();

        return repository.save(friendRequesting);
    }

    private Friend getFriendStatusAccepted() {
        final Date now = CommonUtils.getDate();
        Account account1 = SampleTestData.randomUserAccountRaw();
        Account account2 = SampleTestData.randomUserAccountRaw();

        accountRepository.save(account1);
        accountRepository.save(account2);

        Friend friendAccepted = new Friend().toBuilder()
                .accountFrom(account1)
                .accountTo(account2)
                .status(FriendStatus.ACCEPTED)
                .requestedAt(now)
                .build();

        return repository.save(friendAccepted);
    }
}
