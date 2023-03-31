package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.dao.AccountRepository;
import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.service.impl.AccountServiceImpl;
import com.mangobyte.accountservice.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    AccountServiceImpl service;

    @Mock
    AccountRepository repository;

    @Test
    void findAll_exist() {
        when(repository.findAll()).thenReturn(SampleTestData.ACCOUNTS);
        assertEquals(service.findAll(), SampleTestData.ACCOUNTS);
    }

    @Test
    void findAll_empty() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(service.findAll(), new ArrayList<>());
    }

    @Test
    void findById_exist() {
        when(repository.findById(SampleTestData.ADMIN_ID)).thenReturn(Optional.of(SampleTestData.ADMIN_ACCOUNT));
        assertEquals(service.findById(SampleTestData.ADMIN_ID), Optional.of(SampleTestData.ADMIN_ACCOUNT));
    }

    @Test
    void findById_notFound() {
        when(repository.findById(111L)).thenReturn(Optional.empty());
        assertEquals(service.findById(111L), Optional.empty());
    }

    @Test
    void findById_null() {
        assertThrows(NullPointerException.class, () -> service.findById(null));
    }

    @Test
    void save_success() {
        Account accountPassRaw = SampleTestData.USER_ACCOUNT.toBuilder()
                .id(null)
                .password(SampleTestData.USER_PASS)
                .createdAt(null)
                .updatedAt(null)
                .build();
        Account accountPassEncoded = accountPassRaw.toBuilder()
                .password(SampleTestData.USER_ACCOUNT.getPassword())
                .build();
        try (MockedStatic<CommonUtils> mocked = mockStatic(CommonUtils.class)) {
            mocked.when(() -> CommonUtils.encodedPassword(SampleTestData.USER_PASS)).thenReturn(SampleTestData.USER_ACCOUNT.getPassword());
            when(repository.save(eq(accountPassEncoded))).thenReturn(SampleTestData.USER_ACCOUNT);
            assertEquals(service.save(accountPassRaw), SampleTestData.USER_ACCOUNT);
        }
    }

    @Test
    void save_null() {
        assertThrows(NullPointerException.class, () -> service.save(null));
    }

    @Test
    void findFirstByUsername_exist() {
        when(repository.findFirstByUsername(SampleTestData.USER_USERNAME)).thenReturn(Optional.of(SampleTestData.USER_ACCOUNT));
        assertEquals(service.findFirstByUsername(SampleTestData.USER_USERNAME), Optional.of(SampleTestData.USER_ACCOUNT));
    }

    @Test
    void findFirstByUsername_notFound() {
        when(repository.findFirstByUsername("who..")).thenReturn(Optional.empty());
        assertEquals(service.findFirstByUsername("who.."), Optional.empty());
    }

    @Test
    void findFirstByUsername_null() {
        assertThrows(NullPointerException.class, () -> service.findFirstByUsername(null));
    }
}
