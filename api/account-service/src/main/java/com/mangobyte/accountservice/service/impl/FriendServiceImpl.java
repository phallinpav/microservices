package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.dao.BlockRepository;
import com.mangobyte.accountservice.dao.FriendRepository;
import com.mangobyte.accountservice.exception.CustomException;
import com.mangobyte.accountservice.model.FriendStatus;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Block;
import com.mangobyte.accountservice.model.entity.Friend;
import com.mangobyte.accountservice.service.FriendService;
import com.mangobyte.accountservice.utils.CommonUtils;
import com.mangobyte.accountservice.utils.ConvertUtils;
import com.mangobyte.accountservice.utils.ErrorMessageUtils;
import com.mangobyte.accountservice.view.FriendRequestView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final BlockRepository blockRepository;

    @Override
    public List<FriendRequestView> getListRequest(Long userAccId, Boolean fromMe) {
        List<Friend> listRequest;

        if (fromMe) {
            listRequest = friendRepository.findRequestingFrom(userAccId);
        } else {
            listRequest = friendRepository.findRequestingTo(userAccId);
        }

        return ConvertUtils.convertToFriendRequestView(listRequest, fromMe);
    }

    @Override
    public void add(Account userAcc, Account otherUserAcc) {

        isRequestSelfAcc(userAcc.getId(), otherUserAcc.getId());
        isBlocked(userAcc.getId(), otherUserAcc.getId());

        Optional<Friend> result = friendRepository.findAcceptedOrRequesting(userAcc.getId(), otherUserAcc.getId());
        if (result.isPresent()) {
            Friend friend = result.get();
            switch (friend.getStatus()) {
                case ACCEPTED -> throw new CustomException(HttpStatus.CONFLICT, ErrorMessageUtils.ALREADY_FRIEND);
                case REQUESTING -> throw new CustomException(HttpStatus.CONFLICT, ErrorMessageUtils.ALREADY_REQUESTED);
                default ->
                        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageUtils.INTERNAL_ERROR);
            }

        } else {
            Friend friend = new Friend();
            friend.setAccountFrom(userAcc);
            friend.setAccountTo(otherUserAcc);
            friend.setStatus(FriendStatus.REQUESTING);
            friendRepository.save(friend);
        }
    }

    @Override
    public void accept(Account userAcc, Account otherUserAcc) {
        responseRequest(userAcc, otherUserAcc, FriendStatus.ACCEPTED);
    }

    @Override
    public void deny(Account userAcc, Account otherUserAcc) {
        responseRequest(userAcc, otherUserAcc, FriendStatus.DENIED);
    }

    @Override
    public void unfriend(Account userAcc, Account otherUserAcc) {
        responseRequest(userAcc, otherUserAcc, FriendStatus.REMOVED);
    }

    private void responseRequest(Account toUserAcc, Account fromOtherAcc, FriendStatus status) {
        isRequestSelfAcc(toUserAcc.getId(), fromOtherAcc.getId());
        isBlocked(toUserAcc.getId(), fromOtherAcc.getId());

        switch (status) {
            case ACCEPTED, DENIED -> {
                Friend friendRequest = friendRepository.findPendingRequest(fromOtherAcc.getId(), toUserAcc.getId())
                        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.REQUEST_NOT_FOUND));

                friendRequest.setStatus(status);
                friendRequest.setRespondedAt(CommonUtils.getDate());
                friendRepository.save(friendRequest);
            }

            case REMOVED -> {
                Friend friend = friendRepository.findAccepted(fromOtherAcc.getId(), toUserAcc.getId())
                        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.FRIEND_NOT_FOUND));

                friend.setRemovedBy(toUserAcc);
                friend.setStatus(status);
                friend.setRemovedAt(CommonUtils.getDate());
                friendRepository.save(friend);
            }

            default -> throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.INVALID_STATUS);
        }
    }

    private void isBlocked(long userAccId, long otherUserAccId) {
        Optional<Block> result = blockRepository.findIsBlocked(userAccId, otherUserAccId);
        if (result.isPresent()) {
            Block blockDetail = result.get();

            long blockingAccId = blockDetail.getAccountFrom().getId();

            if (userAccId == blockingAccId) {
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.BLOCKING_THIS_ACCOUNT);
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.BLOCKED_FROM_THIS_ACCOUNT);
            }
        }
    }

    private void isRequestSelfAcc(long userAccId, long otherUserAccId) {
        if (otherUserAccId == userAccId) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.INVALID_SELF_REQUESTING);
        }
    }

}
