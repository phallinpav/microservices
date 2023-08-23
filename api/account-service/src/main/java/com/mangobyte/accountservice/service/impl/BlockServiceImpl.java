package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.dao.BlockRepository;
import com.mangobyte.accountservice.exception.CustomException;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Block;
import com.mangobyte.accountservice.service.BlockService;
import com.mangobyte.accountservice.service.FriendService;
import com.mangobyte.accountservice.utils.CommonUtils;
import com.mangobyte.accountservice.utils.ErrorMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;
    private final FriendService friendService;

    @Override
    public void block(Account userAcc, Account otherUserAcc) {

        isRequestSelfAcc(userAcc.getId(), otherUserAcc.getId());

        Optional<Block> result = blockRepository.findIsBlocked(userAcc.getId(), otherUserAcc.getId());
        if (result.isPresent()) {
            Block blockDetail = result.get();

            long blockingAccId = blockDetail.getAccountFrom().getId();

            if (userAcc.getId() == blockingAccId) {
                throw new CustomException(HttpStatus.CONFLICT, ErrorMessageUtils.BLOCKING_THIS_ACCOUNT);
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.BLOCKED_FROM_THIS_ACCOUNT);
            }

        } else {
            friendService.unfriend(userAcc, otherUserAcc);
            Block blockRecord = new Block();
            blockRecord.setAccountFrom(userAcc);
            blockRecord.setAccountTo(otherUserAcc);
            blockRecord.setIsBlocked(true);
            blockRecord.setBlockedAt(CommonUtils.getDate());
            blockRepository.save(blockRecord);
        }
    }

    private void isRequestSelfAcc(long userAccId, long otherUserAccId) {
        if (otherUserAccId == userAccId) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessageUtils.INVALID_SELF_REQUESTING);
        }
    }
}
