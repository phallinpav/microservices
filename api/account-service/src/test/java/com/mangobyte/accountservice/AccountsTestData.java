package com.mangobyte.accountservice;

import com.mangobyte.accountservice.model.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class AccountsTestData {
    public final static Account ADMIN_IS_FRIEND = getAdminIsFriend();
    public final static Account USER_IS_FRIEND = getUserIsFriend();
    public final static Account ADMIN_IS_NOT_FRIEND = getAdminIsNotFriend();
    public final static Account USER_IS_NOT_FRIEND = getUserIsNotFriend();
    public final static Page<Account> IS_FRIEND_LIST = getPageIsFriend();
    public final static Page<Account> NOT_FRIEND_LIST = getPageIsNotFriend();
    public final static Pageable PAGING = PageRequest.of(0, 10);

    private static Page<Account> getPageIsNotFriend() {
        return new PageImpl<>(Arrays.asList(ADMIN_IS_NOT_FRIEND, USER_IS_NOT_FRIEND));
    }

    private static Page<Account> getPageIsFriend() {
        return new PageImpl<>(Arrays.asList(ADMIN_IS_FRIEND, USER_IS_FRIEND));
    }

    private static Account getAdminIsFriend() {
        return Account.builder()
                .id(SampleTestData.ADMIN_ID)
                .username(SampleTestData.ADMIN_USERNAME)
                .isFriend(true)
                .build();
    }

    private static Account getUserIsFriend() {
        return Account.builder()
                .id(SampleTestData.USER_ID)
                .username(SampleTestData.USER_USERNAME)
                .isFriend(true)
                .build();
    }

    private static Account getAdminIsNotFriend() {
        return Account.builder()
                .id(SampleTestData.ADMIN_ID)
                .username(SampleTestData.ADMIN_USERNAME)
                .isFriend(false)
                .build();
    }

    private static Account getUserIsNotFriend() {
        return Account.builder()
                .id(SampleTestData.USER_ID)
                .username(SampleTestData.USER_USERNAME)
                .isFriend(false)
                .build();
    }
}

