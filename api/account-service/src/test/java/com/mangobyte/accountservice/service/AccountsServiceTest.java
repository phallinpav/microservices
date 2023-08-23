package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.AccountsTestData;
import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.dao.AccountsRepository;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.impl.AccountsServiceImpl;
import com.mangobyte.accountservice.utils.ConvertUtils;
import com.mangobyte.accountservice.view.AccountsView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountsServiceTest {

    @InjectMocks
    AccountsServiceImpl accountsService;

    @Mock
    AccountsRepository accountsRepository;

    @Test
    void search_account_username_empty() {
        when(accountsRepository.getAccounts(SampleTestData.ADMIN_ACCOUNT.getId(), AccountsTestData.PAGING)).thenReturn(AccountsTestData.IS_FRIEND_LIST);
        accountsService.search(SampleTestData.ADMIN_ACCOUNT, Optional.empty());

        verify(accountsRepository, times(1)).getAccounts(SampleTestData.ADMIN_ID, AccountsTestData.PAGING);
        verify(accountsRepository, times(0)).getAccountsByUsername(SampleTestData.ADMIN_ID, Optional.empty().toString(), AccountsTestData.PAGING);
    }

    @Test
    void search_account_with_username() {
        when(accountsRepository.getAccountsByUsername(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_USERNAME, AccountsTestData.PAGING)).thenReturn(AccountsTestData.IS_FRIEND_LIST);

        accountsService.search(SampleTestData.ADMIN_ACCOUNT, Optional.of(SampleTestData.USER_USERNAME));

        verify(accountsRepository, times(0)).getAccounts(SampleTestData.ADMIN_ID, AccountsTestData.PAGING);
        verify(accountsRepository, times(1)).getAccountsByUsername(SampleTestData.ADMIN_ID, SampleTestData.USER_USERNAME, AccountsTestData.PAGING);
    }

    @Test
    void isConvert_to_accountsView_isFriend() {
        when(accountsRepository.getAccounts(SampleTestData.ADMIN_ACCOUNT.getId(), AccountsTestData.PAGING)).thenReturn(AccountsTestData.IS_FRIEND_LIST);
        Page<Account> accounts = accountsRepository.getAccounts(SampleTestData.ADMIN_ID, AccountsTestData.PAGING);
        List<AccountsView> accountsViewList = ConvertUtils.convertToAccountsView(accounts.getContent());
        for (AccountsView view : accountsViewList) {
            assertEquals(1L, view.id());
            assertEquals(SampleTestData.ADMIN_USERNAME, view.username());
            assertEquals(true, view.isFriend());
            break;
        }
    }

    @Test
    void isConvert_to_accountsView_isNotFriend() {
        when(accountsRepository.getAccounts(SampleTestData.ADMIN_ACCOUNT.getId(), AccountsTestData.PAGING)).thenReturn(AccountsTestData.NOT_FRIEND_LIST);
        Page<Account> accounts = accountsRepository.getAccounts(SampleTestData.ADMIN_ID, AccountsTestData.PAGING);
        List<AccountsView> accountsViewList = ConvertUtils.convertToAccountsView(accounts.getContent());
        for (AccountsView view : accountsViewList) {
            assertEquals(1L, view.id());
            assertEquals(SampleTestData.ADMIN_USERNAME, view.username());
            assertEquals(false, view.isFriend());
            break;
        }
    }


}
