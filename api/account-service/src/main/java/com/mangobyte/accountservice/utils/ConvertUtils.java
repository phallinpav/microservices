package com.mangobyte.accountservice.utils;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Friend;
import com.mangobyte.accountservice.view.AccountsView;
import com.mangobyte.accountservice.view.FriendRequestView;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {

    public static List<FriendRequestView> convertToFriendRequestView(List<Friend> friends, Boolean fromMe) {
        List<FriendRequestView> friendRequestViewList = new ArrayList<>();
        for (Friend friend : friends) {
            if (fromMe) {
                friendRequestViewList.add(new FriendRequestView(friend.getAccountTo().getId(), friend.getAccountTo().getUsername(), friend.getRequestedAt().toString()));
            } else {
                friendRequestViewList.add(new FriendRequestView(friend.getAccountFrom().getId(), friend.getAccountFrom().getUsername(), friend.getRequestedAt().toString()));
            }
        }
        return friendRequestViewList;
    }

    public static List<AccountsView> convertToAccountsView(List<Account> accounts) {
        List<AccountsView> accountsViewList = new ArrayList<>();
        for (Account account : accounts) {
            accountsViewList.add(new AccountsView(account.getId(), account.getUsername(), account.getIsFriend()));
        }
        return accountsViewList;
    }

}
