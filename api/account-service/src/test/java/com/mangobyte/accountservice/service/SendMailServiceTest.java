package com.mangobyte.accountservice.service;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.impl.SendMailServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SendMailServiceTest {

    @InjectMocks
    SendMailServiceImpl service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "apiKey", "api-B62171B2D90E11EDB62DF23C91C88F4E");
        ReflectionTestUtils.setField(service, "sender", "Phallin <phallin@phallin.xyz>");
        ReflectionTestUtils.setField(service, "templateId", "4937131");
        ReflectionTestUtils.setField(service, "replyTo", "phallin@mango-byte.com");
        ReflectionTestUtils.setField(service, "endpoint", "https://api.smtp2go.com/v3/email/send");
        ReflectionTestUtils.setField(service, "apiServer", "http://localhost:8080");
    }

    @Disabled("don't want to auto test this send mail, because it will send mail for real")
    @Test
    void sendConfirmationEmail() {
        Account account = Account.builder()
            .id(10l)
            .username("Phallin")
            .email("phallinpav77@gmail.com")
            .build();
        service.sendConfirmationEmail(account);
    }

    @Test
    void decodeKey() {
        Account account = Account.builder()
            .id(10l)
            .username("Phallin")
            .email("phallinpav77@gmail.com")
            .build();
        String key = generateEncodedKey(account);
        assertEquals(Pair.of(10l, "phallinpav77@gmail.com"), service.decodeKey(key));

        account.setId(null);
        account.setEmail("abcdedfhijk@gmail.com");
        key = generateEncodedKey(account);
        assertEquals(Pair.of(0l, "abcdedfhijk@gmail.com"), service.decodeKey(key));
    }

    // make sure to check this method in SendMailServiceImpl if there were any change in encoding
    private String generateEncodedKey(Account account) {
        String key = String.format("%d,%s", account.getId(), account.getEmail());
        return Base64.getUrlEncoder().encodeToString(key.getBytes());
    }

}
