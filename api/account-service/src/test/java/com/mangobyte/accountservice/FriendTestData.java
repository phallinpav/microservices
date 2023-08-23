package com.mangobyte.accountservice;

import com.mangobyte.accountservice.model.FriendStatus;
import com.mangobyte.accountservice.model.entity.Friend;
import com.mangobyte.accountservice.utils.CommonUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FriendTestData {

    public final static Friend ADMIN_REQUEST_TO_USER = adminRequestFriendToUser();
    public final static Friend ADMIN_REQUEST_TO_RANDOM_ACC = adminRequestFriendToRandomAccount();
    public final static List<Friend> FRIEND_LIST_REQUEST_FROM_ADMIN = Arrays.asList(ADMIN_REQUEST_TO_USER, ADMIN_REQUEST_TO_RANDOM_ACC);
    public final static Friend USER_REQUEST_TO_ADMIN = userRequestFriendToAdmin();
    public final static Friend RANDOM_ACC_REQUEST_TO_ADMIN = randomAccountRequestFriendToAdmin();
    public final static List<Friend> FRIEND_LIST_REQUEST_TO_ADMIN = Arrays.asList(USER_REQUEST_TO_ADMIN, RANDOM_ACC_REQUEST_TO_ADMIN);

    public static Friend adminRequestFriendToUser() {
        Friend friend = new Friend();
        final Date now = CommonUtils.getDate();
        friend.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        friend.setAccountTo(SampleTestData.USER_ACCOUNT);
        friend.setStatus(FriendStatus.REQUESTING);
        friend.setRequestedAt(now);
        return friend;
    }

    public static Friend userAcceptedRequestFromAdmin() {
        final Date now = CommonUtils.getDate();
        Friend friend = new Friend();
        friend.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        friend.setAccountTo(SampleTestData.USER_ACCOUNT);
        friend.setStatus(FriendStatus.ACCEPTED);
        friend.setRequestedAt(now);
        return friend;
    }

    public static Friend userDenyRequestFromAdmin() {
        final Date now = CommonUtils.getDate();
        Friend friend = new Friend();
        friend.setId(1L);
        friend.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        friend.setAccountTo(SampleTestData.USER_ACCOUNT);
        friend.setStatus(FriendStatus.DENIED);
        friend.setRequestedAt(now);
        return friend;
    }

    public static Friend adminRemovedUserFromFriend() {
        final Date now = CommonUtils.getDate();
        Friend friend = new Friend();
        friend.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        friend.setAccountTo(SampleTestData.USER_ACCOUNT);
        friend.setStatus(FriendStatus.REMOVED);
        friend.setRespondedAt(now);
        friend.setRemovedBy(SampleTestData.ADMIN_ACCOUNT);
        return friend;
    }

    public static Friend userRemovedAdminFromFriend() {
        final Date now = CommonUtils.getDate();
        Friend friend = new Friend();
        friend.setId(1L);
        friend.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        friend.setAccountTo(SampleTestData.USER_ACCOUNT);
        friend.setStatus(FriendStatus.REMOVED);
        friend.setRespondedAt(now);
        friend.setRemovedBy(SampleTestData.USER_ACCOUNT);
        return friend;
    }

    public static Friend userRequestFriendToAdmin() {
        final Date now = CommonUtils.getDate();
        Friend friend = new Friend();
        friend.setAccountFrom(SampleTestData.USER_ACCOUNT);
        friend.setAccountTo(SampleTestData.ADMIN_ACCOUNT);
        friend.setStatus(FriendStatus.REQUESTING);
        friend.setRequestedAt(now);
        return friend;
    }

    public static Friend adminRequestFriendToRandomAccount() {
        final Date now = CommonUtils.getDate();
        Friend friend = new Friend();
        friend.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        friend.setAccountTo(SampleTestData.randomUserAccountRaw());
        friend.setStatus(FriendStatus.REQUESTING);
        friend.setRequestedAt(now);
        return friend;
    }

    public static Friend randomAccountRequestFriendToAdmin() {
        final Date now = CommonUtils.getDate();
        Friend friend = new Friend();
        friend.setAccountFrom(SampleTestData.randomUserAccountRaw());
        friend.setAccountTo(SampleTestData.ADMIN_ACCOUNT);
        friend.setStatus(FriendStatus.REQUESTING);
        friend.setRequestedAt(now);
        return friend;
    }
}
