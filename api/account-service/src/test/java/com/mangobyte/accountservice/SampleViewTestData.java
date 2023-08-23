package com.mangobyte.accountservice;

import com.mangobyte.accountservice.utils.CommonUtils;
import com.mangobyte.accountservice.view.AccountsView;
import com.mangobyte.accountservice.view.FriendRequestView;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SampleViewTestData {
    public final static AccountsView ACCOUNT_FIELD_VIEW = getAccountFieldView();
    public final static List<AccountsView> ACCOUNT_FIELD_VIEW_LIST = Arrays.asList(ACCOUNT_FIELD_VIEW, ACCOUNT_FIELD_VIEW);
    public final static FriendRequestView FRIEND_REQUEST_VIEW = getFriendRequestView();
    public final static List<FriendRequestView> FRIEND_REQUEST_VIEWS_LIST = Arrays.asList(FRIEND_REQUEST_VIEW, FRIEND_REQUEST_VIEW);

    private static FriendRequestView getFriendRequestView() {
        final Date now = CommonUtils.getDate();
        return new FriendRequestView(1L, RandomStringUtils.randomAlphanumeric(3, 30), now.toString());
    }

    private static AccountsView getAccountFieldView() {
        return new AccountsView(1L, RandomStringUtils.randomAlphanumeric(3, 30), true);
    }
}
