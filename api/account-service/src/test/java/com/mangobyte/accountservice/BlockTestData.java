package com.mangobyte.accountservice;

import com.mangobyte.accountservice.model.entity.Block;
import com.mangobyte.accountservice.utils.CommonUtils;

import java.util.Date;

public class BlockTestData {
    public static Block adminBlockToUser() {
        final Date now = CommonUtils.getDate();
        Block block = new Block();
        block.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        block.setAccountTo(SampleTestData.USER_ACCOUNT);
        block.setIsBlocked(true);
        block.setBlockedAt(now);
        return block;
    }

    public static Block userBlockToAdmin() {
        final Date now = CommonUtils.getDate();
        Block block = new Block();
        block.setAccountFrom(SampleTestData.USER_ACCOUNT);
        block.setAccountTo(SampleTestData.ADMIN_ACCOUNT);
        block.setIsBlocked(true);
        block.setBlockedAt(now);
        return block;
    }

    public static Block adminUnblockToUser() {
        final Date now = CommonUtils.getDate();
        Block block = new Block();
        block.setAccountFrom(SampleTestData.ADMIN_ACCOUNT);
        block.setAccountTo(SampleTestData.USER_ACCOUNT);
        block.setIsBlocked(false);
        block.setBlockedAt(now);
        block.setUnblockedAt(now);
        return block;
    }

    public static Block userUnblockToAdmin() {
        final Date now = CommonUtils.getDate();
        Block block = new Block();
        block.setAccountFrom(SampleTestData.USER_ACCOUNT);
        block.setAccountTo(SampleTestData.ADMIN_ACCOUNT);
        block.setIsBlocked(false);
        block.setBlockedAt(now);
        block.setUnblockedAt(now);
        return block;
    }

    public static Block unknownBlockData() {
        final Date now = CommonUtils.getDate();
        Block block = new Block();
        block.setAccountFrom(SampleTestData.UNKNOWN_ACCOUNT);
        block.setAccountTo(SampleTestData.UNKNOWN_ACCOUNT);
        block.setIsBlocked(true);
        block.setBlockedAt(now);
        return block;
    }
}
