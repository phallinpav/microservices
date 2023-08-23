package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.dao.TokenRepository;
import com.mangobyte.accountservice.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    TokenServiceImpl service;

    @Mock
    TokenRepository repository;

    @Test
    void generateToken_success_noExistingToken() {
        when(repository.findByAccountId(eq(SampleTestData.ADMIN_ID))).thenReturn(Optional.empty());
        when(repository.save(eq(SampleTestData.ADMIN_TOKEN))).thenReturn(SampleTestData.ADMIN_TOKEN);
        assertEquals(service.generate(SampleTestData.ADMIN_TOKEN), SampleTestData.ADMIN_TOKEN);
        verify(repository, times(1)).findByAccountId(any());
        verify(repository, times(0)).deleteByAccountId(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    void generateToken_success_withExistingAdminToken() {
        when(repository.findByAccountId(eq(SampleTestData.ADMIN_ID))).thenReturn(Optional.of(SampleTestData.ADMIN_TOKEN));
        assertEquals(service.generate(SampleTestData.ADMIN_TOKEN), SampleTestData.ADMIN_TOKEN);
        verify(repository, times(1)).findByAccountId(any());
        verify(repository, times(0)).deleteByAccountId(any());
        verify(repository, times(0)).save(any());
    }

    @Test
    void generateToken_success_withExistingUserToken() {
        when(repository.findByAccountId(eq(SampleTestData.USER_ID))).thenReturn(Optional.of(SampleTestData.USER_TOKEN));
        when(repository.save(eq(SampleTestData.USER_TOKEN))).thenReturn(SampleTestData.USER_TOKEN);
        assertEquals(service.generate(SampleTestData.USER_TOKEN), SampleTestData.USER_TOKEN);
        verify(repository, times(1)).findByAccountId(any());
        verify(repository, times(1)).deleteByAccountId(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    void generateToken_fail_atDelete() {
        when(repository.findByAccountId(eq(SampleTestData.USER_ID))).thenReturn(Optional.of(SampleTestData.USER_TOKEN));
        doThrow(new RuntimeException()).when(repository).deleteByAccountId(eq(SampleTestData.USER_ID));
        assertThrows(RuntimeException.class, () -> service.generate(SampleTestData.USER_TOKEN));
        verify(repository, times(1)).findByAccountId(any());
        verify(repository, times(1)).deleteByAccountId(any());
        verify(repository, times(0)).save(any());
    }

    @Test
    void generateToken_fail_atSave() {
        when(repository.findByAccountId(eq(SampleTestData.USER_ID))).thenReturn(Optional.of(SampleTestData.USER_TOKEN));
        doThrow(new RuntimeException()).when(repository).save(eq(SampleTestData.USER_TOKEN));
        assertThrows(RuntimeException.class, () -> service.generate(SampleTestData.USER_TOKEN));
        verify(repository, times(1)).findByAccountId(any());
        verify(repository, times(1)).deleteByAccountId(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    void generateToken_mixTest() {
        when(repository.findByAccountId(eq(SampleTestData.ADMIN_ID))).thenReturn(Optional.of(SampleTestData.ADMIN_TOKEN));
        when(repository.findByAccountId(eq(SampleTestData.USER_ID))).thenReturn(Optional.empty());
        doThrow(new RuntimeException()).when(repository).save(eq(SampleTestData.USER_TOKEN));
        assertThrows(RuntimeException.class, () -> service.generate(SampleTestData.USER_TOKEN));
        assertNotEquals(service.generate(SampleTestData.ADMIN_TOKEN), SampleTestData.USER_TOKEN);
        verify(repository, times(2)).findByAccountId(any());
        verify(repository, times(0)).deleteByAccountId(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    void generateToken_null() {
        assertThrows(NullPointerException.class, () -> service.generate(null));
    }

    @Test
    void findAccountByToken_found() {
        when(repository.findAccountByToken(eq(SampleTestData.ADMIN_TOKEN_VALUE))).thenReturn(Optional.of(SampleTestData.ADMIN_ACCOUNT));
        assertEquals(service.findAccountByToken(SampleTestData.ADMIN_TOKEN_VALUE), Optional.of(SampleTestData.ADMIN_ACCOUNT));
    }

    @Test
    void findAccountByToken_notFound() {
        when(repository.findAccountByToken(eq(SampleTestData.UNKNOWN_TOKEN_VALUE))).thenReturn(Optional.empty());
        assertEquals(service.findAccountByToken(SampleTestData.UNKNOWN_TOKEN_VALUE), Optional.empty());
    }

    @Test
    void findAccountByToken_null() {
        assertThrows(NullPointerException.class, () -> service.findAccountByToken(null));
    }
}
