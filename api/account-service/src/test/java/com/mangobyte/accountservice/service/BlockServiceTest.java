package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.BlockTestData;
import com.mangobyte.accountservice.ErrorMessageUtils;
import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.dao.BlockRepository;
import com.mangobyte.accountservice.exception.CustomException;
import com.mangobyte.accountservice.model.entity.Block;
import com.mangobyte.accountservice.service.impl.BlockServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTest {

    @InjectMocks
    BlockServiceImpl service;

    @Mock
    BlockRepository blockRepository;

    @Mock
    FriendService friendService;

    @Test
    void block_success() {
        when(blockRepository.findIsBlocked(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_ACCOUNT.getId()))
                .thenReturn(Optional.empty());
        when(blockRepository.save(any())).thenReturn(BlockTestData.adminBlockToUser());

        service.block(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT);

        ArgumentCaptor<Block> cap = ArgumentCaptor.forClass(Block.class);
        verify(blockRepository).save(cap.capture());

        assertEquals(BlockTestData.adminBlockToUser().getAccountFrom(), cap.getValue().getAccountFrom());
        assertEquals(BlockTestData.adminBlockToUser().getAccountTo(), cap.getValue().getAccountTo());
        assertEquals(BlockTestData.adminBlockToUser().getIsBlocked(), cap.getValue().getIsBlocked());
        verify(blockRepository, times(1)).findIsBlocked(any(), any());
        verify(blockRepository, times(1)).save(any());
    }

    @Test
    void block_fail_already_blocked() {
        when(blockRepository.findIsBlocked(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_ACCOUNT.getId()))
                .thenReturn(Optional.of(BlockTestData.adminBlockToUser()));

        final CustomException exception = assertThrows(CustomException.class,
                () -> service.block(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(ErrorMessageUtils.BLOCKING_THIS_ACCOUNT, exception.getMessage());
    }

    @Test
    void save_fail() {
        when(blockRepository.findIsBlocked(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_ACCOUNT.getId()))
                .thenReturn(Optional.empty());
        doThrow(new RuntimeException()).when(blockRepository).save(any());
        assertThrows(RuntimeException.class,
                () -> service.block(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        verify(blockRepository, times(1)).findIsBlocked(any(), any());
        verify(blockRepository, times(1)).save(any());
    }

    @Test
    void block_fail_is_blocked() {
        when(blockRepository.findIsBlocked(SampleTestData.ADMIN_ACCOUNT.getId(), SampleTestData.USER_ACCOUNT.getId()))
                .thenReturn(Optional.of(BlockTestData.userBlockToAdmin()));

        final CustomException exception = assertThrows(CustomException.class,
                () -> service.block(SampleTestData.ADMIN_ACCOUNT, SampleTestData.USER_ACCOUNT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.BLOCKED_FROM_THIS_ACCOUNT, exception.getMessage());
    }

    @Test
    void request_to_yourself() {
        final CustomException exception = assertThrows(CustomException.class,
                () -> service.block(SampleTestData.ADMIN_ACCOUNT, SampleTestData.ADMIN_ACCOUNT));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(ErrorMessageUtils.INVALID_SELF_REQUESTING, exception.getMessage());
    }
}
