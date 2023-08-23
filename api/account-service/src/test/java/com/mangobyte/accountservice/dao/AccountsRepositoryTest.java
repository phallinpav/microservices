package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.AccountsTestData;
import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.model.FriendStatus;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Friend;
import com.mangobyte.accountservice.utils.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountsRepositoryTest {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private FriendRepository friendRepository;

    @BeforeEach
    void setup() {
        clearDatabase();
    }

    private void clearDatabase() {
        accountsRepository.deleteAll();
    }

    @Test
    @DisplayName("Search By Id 2 in list size 2 (Id 1 and 2)")
    void check_size_of_list() {
        Account account1 = getAccount1();
        Account account2 = getAccount2();

        accountsRepository.save(account1);
        accountsRepository.save(account2);
        Page<Account> result = accountsRepository.getAccounts(98L, AccountsTestData.PAGING);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("Search By Id 1 in list size 2 (Id 1 and 2), not hit own Id")
    void not_find_own_id() {
        Account account1 = getAccount1();
        Account account2 = getAccount2();

        accountsRepository.save(account1);
        accountsRepository.save(account2);

        Page<Account> result = accountsRepository.getAccounts(account1.getId(), AccountsTestData.PAGING);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Compare searched data with saved data in order to check findAccounts works")
    void check_return_right_data() {
        Account account1 = getAccount1();
        Account account2 = getAccount2();

        account1.setUsername("aaaa");

        accountsRepository.save(account1);
        accountsRepository.save(account2);

        String searchUsername = "a";
        Page<Account> accounts = accountsRepository.getAccountsByUsername(account2.getId(), searchUsername, AccountsTestData.PAGING);

        for (Account actual : accounts) {
            assertEquals("aaaa", actual.getUsername());
            assertEquals(false, actual.getIsFriend());
            break;
        }

    }

    @Test
    @DisplayName("Check two accounts are status ACCEPTED then isFriend will be true")
    void friend_status_accepted_then_isFriend_true() {
        Account account1 = getAccount1();
        Account account2 = getAccount2();

        accountsRepository.saveAll(List.of(account1, account2));

        List<Account> list = accountsRepository.findAll();
        for (Account account : list) {
            System.out.println(account);
        }

        Friend friend = Friend.builder().accountFrom(account1).accountTo(account2).status(FriendStatus.ACCEPTED).build();

        friendRepository.save(friend);

        Page<Account> accounts = accountsRepository.getAccounts(account2.getId(), AccountsTestData.PAGING);

        for (Account actual : accounts) {
            assertEquals(true, actual.getIsFriend());
            break;
        }
    }

    @Test
    @DisplayName("when input a as username, find data contains a first")
    void find_with_username_a() {
        Account account1 = getAccount1();
        Account account2 = getAccount2();

        account1.setUsername("bbbb");
        account2.setUsername("aaaa");

        accountsRepository.saveAll(List.of(account1, account2));

        long authId = 99L;
        String searchUsername = "a";

        Page<Account> accounts = accountsRepository.getAccountsByUsername(authId, searchUsername, AccountsTestData.PAGING);
        for (Account actual : accounts) {
            assertEquals("aaaa", actual.getUsername());
            break;
        }

    }

    @Test
    @DisplayName("when input b as username, find data contains b first")
    void find_with_username_b() {
        Account account1 = getAccount1();
        Account account2 = getAccount2();

        account1.setUsername("aaaa");
        account2.setUsername("bbbb");

        accountsRepository.saveAll(List.of(account1, account2));

        long authId = 3L;
        String searchUsername = "b";

        Page<Account> accounts = accountsRepository.getAccountsByUsername(authId, searchUsername, AccountsTestData.PAGING);

        for (Account actual : accounts) {
            assertEquals("bbbb", actual.getUsername());
            break;
        }

    }

    private void insertData() {
        Account account1 = getAccount1();
        Account account2 = getAccount2();

        accountsRepository.save(account1);
        accountsRepository.save(account2);
    }

    private Account getAccount1() {
        Date now = CommonUtils.getDate();
        Account account = SampleTestData.randomUserAccountRaw();
        account.setCreatedAt(now);
        return account;
    }

    private Account getAccount2() {
        Date now = CommonUtils.getDate();
        Account account = SampleTestData.randomUserAccountRaw();
        account.setCreatedAt(now);
        return account;
    }
}

