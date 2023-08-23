package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.BlockTestData;
import com.mangobyte.accountservice.ErrorMessageUtils;
import com.mangobyte.accountservice.FriendTestData;
import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.dao.BlockRepository;
import com.mangobyte.accountservice.dao.FriendRepository;
import com.mangobyte.accountservice.exception.CustomException;
import com.mangobyte.accountservice.model.FriendStatus;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Friend;
import com.mangobyte.accountservice.service.impl.FriendServiceImpl;
import com.mangobyte.accountservice.utils.ConvertUtils;
import com.mangobyte.accountservice.view.FriendRequestView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @InjectMocks
    FriendServiceImpl service;

    @Mock
    FriendRepository friendRepository;

    @Mock
    BlockRepository blockRepository;

    @Test
    void add_isPresent_test() {
        doReturn(Optional.empty()).when(friendRepository).findAcceptedOrRequesting(any(), any());
        Optional<Friend> actual = friendRepository.findAcceptedOrRequesting(any(), any());

        service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT);

        ArgumentCaptor<Friend> cap = ArgumentCaptor.forClass(Friend.class);
        verify(friendRepository).save(cap.capture());

        assertFalse(actual.isPresent());
        assertEquals(FriendTestData.adminRequestFriendToUser().getAccountFrom(), cap.getValue().getAccountFrom());
        assertEquals(FriendTestData.adminRequestFriendToUser().getAccountTo(), cap.getValue().getAccountTo());
        assertEquals(FriendTestData.adminRequestFriendToUser().getStatus(), cap.getValue().getStatus());
    }

    @Test
    void add_success() {
        when(friendRepository.findAcceptedOrRequesting(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_ACCOUNT.getId()))
                .thenReturn(Optional.empty());
        when(friendRepository.save(any())).thenReturn(FriendTestData.adminRequestFriendToUser());

        service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT);

        ArgumentCaptor<Friend> cap = ArgumentCaptor.forClass(Friend.class);
        verify(friendRepository).save(cap.capture());

        assertEquals(FriendTestData.adminRequestFriendToUser().getAccountFrom(), cap.getValue().getAccountFrom());
        assertEquals(FriendTestData.adminRequestFriendToUser().getAccountTo(), cap.getValue().getAccountTo());
        assertEquals(FriendTestData.adminRequestFriendToUser().getStatus(), cap.getValue().getStatus());
        verify(friendRepository, times(1)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(1)).save(any());
    }

    @Test
    void add_success_from_user() {
        when(friendRepository.findAcceptedOrRequesting(SampleTestData.USER_ACCOUNT.getId(), SampleTestData.ADMIN_ACCOUNT.getId()))
                .thenReturn(Optional.empty());
        when(friendRepository.save(any())).thenReturn(FriendTestData.userRequestFriendToAdmin());

        service.add(SampleTestData.USER_ACCOUNT, SampleTestData.ADMIN_ACCOUNT);

        ArgumentCaptor<Friend> cap = ArgumentCaptor.forClass(Friend.class);
        verify(friendRepository).save(cap.capture());

        assertEquals(FriendTestData.userRequestFriendToAdmin().getAccountFrom(), cap.getValue().getAccountFrom());
        assertEquals(FriendTestData.userRequestFriendToAdmin().getAccountTo(), cap.getValue().getAccountTo());
        assertEquals(FriendTestData.userRequestFriendToAdmin().getStatus(), cap.getValue().getStatus());
        verify(friendRepository, times(1)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(1)).save(any());
    }

    @Test
    void add_fail_duplicated_request() {
        when(friendRepository.findAcceptedOrRequesting(any(), any()))
                .thenReturn(Optional.of(FriendTestData.adminRequestFriendToUser()));

        final CustomException exception = assertThrows(CustomException.class,
                () -> service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(ErrorMessageUtils.ALREADY_REQUESTED_MESSAGE, exception.getMessage());
        verify(friendRepository, times(1)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void add_fail_already_friend() {
        when(friendRepository.findAcceptedOrRequesting(any(), any()))
                .thenReturn(Optional.of(FriendTestData.userAcceptedRequestFromAdmin()));

        final CustomException exception = assertThrows(CustomException.class,
                () -> service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(ErrorMessageUtils.ALREADY_FRIEND_MESSAGE, exception.getMessage());
        verify(friendRepository, times(1)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void add_fail_not_required_status() {
        when(friendRepository.findAcceptedOrRequesting(any(), any()))
                .thenReturn(Optional.of(FriendTestData.userAcceptedRequestFromAdmin().toBuilder().status(FriendStatus.DENIED).build()));
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals(ErrorMessageUtils.INTERNAL_ERROR, exception.getMessage());
        verify(friendRepository, times(1)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void add_fail_save() {
        when(friendRepository.findAcceptedOrRequesting(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_ACCOUNT.getId()))
                .thenReturn(Optional.empty());
        doThrow(new RuntimeException()).when(friendRepository).save(any());
        assertThrows(RuntimeException.class,
                () -> service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        verify(friendRepository, times(1)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(1)).save(any());
    }

    @Test
    void add_fail_add_yourself() {
        CustomException exception = assertThrows(CustomException.class,
                () -> service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.ADMIN_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.INVALID_SELF_REQUESTING, exception.getMessage());
    }

    @Test
    void accept_fail_request_to_yourself() {
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.accept(SampleTestData.ADMIN_ACCOUNT, SampleTestData.ADMIN_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.INVALID_SELF_REQUESTING, exception.getMessage());
        verify(friendRepository, times(0)).findAccepted(any(), any());
        verify(friendRepository, times(0)).findPendingRequest(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void deny_fail_request_to_yourself() {
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.deny(SampleTestData.ADMIN_ACCOUNT, SampleTestData.ADMIN_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.INVALID_SELF_REQUESTING, exception.getMessage());
        verify(friendRepository, times(0)).findAccepted(any(), any());
        verify(friendRepository, times(0)).findPendingRequest(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void unfriend_fail_request_to_yourself() {
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.unfriend(SampleTestData.ADMIN_ACCOUNT, SampleTestData.ADMIN_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.INVALID_SELF_REQUESTING, exception.getMessage());
        verify(friendRepository, times(0)).findAccepted(any(), any());
        verify(friendRepository, times(0)).findPendingRequest(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void accept_fail_not_found_status_requesting() {
        when(friendRepository.findPendingRequest(any(), any())).thenReturn(Optional.empty());
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.accept(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.REQUEST_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(friendRepository, times(0)).findAccepted(any(), any());
        verify(friendRepository, times(1)).findPendingRequest(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void deny_fail_not_found_status_requesting() {
        when(friendRepository.findPendingRequest(any(), any())).thenReturn(Optional.empty());
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.deny(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.REQUEST_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(friendRepository, times(0)).findAccepted(any(), any());
        verify(friendRepository, times(1)).findPendingRequest(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void accept_success() {
        when(friendRepository.findPendingRequest(SampleTestData.USER_ACCOUNT.getId(), SampleTestData.ADMIN_ACCOUNT.getId()))
                .thenReturn(Optional.of(FriendTestData.userAcceptedRequestFromAdmin()));

        when(friendRepository.save(any())).thenReturn(FriendTestData.userAcceptedRequestFromAdmin());

        service.accept(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT);

        ArgumentCaptor<Friend> cap = ArgumentCaptor.forClass(Friend.class);
        verify(friendRepository).save(cap.capture());

        assertEquals(FriendTestData.userAcceptedRequestFromAdmin().getAccountFrom(), cap.getValue().getAccountFrom());
        assertEquals(FriendTestData.userAcceptedRequestFromAdmin().getAccountTo(), cap.getValue().getAccountTo());
        assertEquals(FriendTestData.userAcceptedRequestFromAdmin().getStatus(), cap.getValue().getStatus());
        verify(friendRepository, times(0)).findAccepted(any(), any());
        verify(friendRepository, times(1)).findPendingRequest(any(), any());
        verify(friendRepository, times(1)).save(any());
    }

    @Test
    void deny_success() {
        when(friendRepository.findPendingRequest(SampleTestData.USER_ACCOUNT.getId(), SampleTestData.ADMIN_ACCOUNT.getId()))
                .thenReturn(Optional.of(FriendTestData.adminRequestFriendToUser()));
        when(friendRepository.save(any())).thenReturn(FriendTestData.userDenyRequestFromAdmin());

        service.deny(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT);

        ArgumentCaptor<Friend> cap = ArgumentCaptor.forClass(Friend.class);
        verify(friendRepository).save(cap.capture());

        assertEquals(FriendTestData.userDenyRequestFromAdmin().getAccountFrom(), cap.getValue().getAccountFrom());
        assertEquals(FriendTestData.userDenyRequestFromAdmin().getAccountTo(), cap.getValue().getAccountTo());
        assertEquals(FriendTestData.userDenyRequestFromAdmin().getStatus(), cap.getValue().getStatus());
        verify(friendRepository, times(1)).findPendingRequest(any(), any());
        verify(friendRepository, times(1)).save(any());
    }

    @Test
    void unfriend_success() {
        when(friendRepository.findAccepted(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_ACCOUNT.getId()))
                .thenReturn(Optional.of(FriendTestData.userAcceptedRequestFromAdmin()));
        when(friendRepository.save(any())).thenReturn(FriendTestData.adminRemovedUserFromFriend());

        service.unfriend(SampleTestData.USER_ACCOUNT, SampleTestData.ADMIN_ACCOUNT);

        ArgumentCaptor<Friend> cap = ArgumentCaptor.forClass(Friend.class);
        verify(friendRepository).save(cap.capture());

        assertEquals(FriendTestData.adminRemovedUserFromFriend().getAccountFrom(), cap.getValue().getAccountFrom());
        assertEquals(FriendTestData.adminRemovedUserFromFriend().getAccountTo(), cap.getValue().getAccountTo());
        assertEquals(FriendTestData.adminRemovedUserFromFriend().getStatus(), cap.getValue().getStatus());
        verify(friendRepository, times(1)).findAccepted(any(), any());
        verify(friendRepository, times(1)).save(any());
    }

    @Test
    void unfriend_fail_friend_not_found() {
        when(friendRepository.findAccepted(any(), any())).thenReturn(Optional.empty());
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.unfriend(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.FRIEND_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(friendRepository, times(1)).findAccepted(any(), any());
        verify(friendRepository, times(0)).save(any());
    }


    @Test
    void add_fail_blocking_target_account() {
        when(blockRepository.findIsBlocked(any(), any())).thenReturn(Optional.of(BlockTestData.adminBlockToUser()));
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.BLOCKING_THIS_ACCOUNT, exception.getMessage());
        verify(friendRepository, times(0)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void add_fail_account_is_blocked() {
        when(blockRepository.findIsBlocked(any(), any())).thenReturn(Optional.of(BlockTestData.userBlockToAdmin()));
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.add(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.BLOCKED_FROM_THIS_ACCOUNT, exception.getMessage());
        verify(friendRepository, times(0)).findAcceptedOrRequesting(any(), any());
        verify(friendRepository, times(0)).save(any());
    }

    @Test
    void has_called_findRequestingFrom() {
        when(friendRepository.findRequestingFrom(any())).thenReturn(FriendTestData.FRIEND_LIST_REQUEST_FROM_ADMIN);
        service.getListRequest(SampleTestData.ADMIN_ID, true);

        verify(friendRepository, times(1)).findRequestingFrom(any());
        verify(friendRepository, times(0)).findRequestingTo(any());
    }

    @Test
    void has_called_findRequestingTo() {
        when(friendRepository.findRequestingTo(any())).thenReturn(FriendTestData.FRIEND_LIST_REQUEST_FROM_ADMIN);

        service.getListRequest(SampleTestData.ADMIN_ID, false);
        verify(friendRepository, times(0)).findRequestingFrom(any());
        verify(friendRepository, times(1)).findRequestingTo(any());
    }

    @Test
    void has_convert_FriendRequestView_from_list_from_admin() {
        List<FriendRequestView> listOfRequesting = ConvertUtils.convertToFriendRequestView(FriendTestData.FRIEND_LIST_REQUEST_FROM_ADMIN, true);
        for (FriendRequestView view : listOfRequesting) {
            assertEquals(SampleTestData.USER_ID, view.id());
            assertEquals(SampleTestData.USER_USERNAME, view.username());
            break;
        }
    }

    @Test
    void has_convert_FriendRequestView_from_list_to_admin() {
        List<FriendRequestView> listOfRequesting = ConvertUtils.convertToFriendRequestView(FriendTestData.FRIEND_LIST_REQUEST_TO_ADMIN, false);
        for (FriendRequestView view : listOfRequesting) {
            assertEquals(SampleTestData.USER_ID, view.id());
            assertEquals(SampleTestData.USER_USERNAME, view.username());
            break;
        }
    }

    @Test
    void input_invalid_status() throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {
        FriendServiceImpl friendService = new FriendServiceImpl(friendRepository, blockRepository);
        Method method = FriendServiceImpl.class.getDeclaredMethod("responseRequest", Account.class, Account.class, FriendStatus.class);
        method.setAccessible(true);

        try {
            method.invoke(friendService, SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT, FriendStatus.REQUESTING);
            fail("Expected exception was not thrown");
        } catch (InvocationTargetException e) {
            assertEquals(CustomException.class, e.getCause().getClass());
            CustomException exception = (CustomException) e.getCause();
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals(ErrorMessageUtils.REQUESTING_BAD_STATUS, exception.getMessage());
            verify(friendRepository, times(0)).findAccepted(any(), any());
            verify(friendRepository, times(0)).findPendingRequest(any(), any());
            verify(friendRepository, times(0)).save(any());
        }
    }

}
