package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.model.SmtpRequest;
import com.mangobyte.accountservice.model.SmtpResponse;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.service.SendMailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    @Value("${mail.apikey}")
    private String apiKey;

    @Value("${mail.sender}")
    private String sender;

    @Value("${mail.templateId}")
    private String templateId;

    @Value("${mail.replyTo}")
    private String replyTo;

    @Value("${mail.endpoint}")
    private String endpoint;

    @Value("${api.server}")
    private String apiServer;

    @Override
    public void sendConfirmationEmail(Account account) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> data = new HashMap<>();
        data.put("username", account.getUsername());
        data.put("confirm_url", generateConfirmUrl(account));
        SmtpRequest smtpRequest = SmtpRequest.builder()
                .api_key(apiKey)
                .to(List.of(account.getUsername() + " <" + account.getEmail() + ">"))
                .sender(sender)
                .template_id(templateId)
                .template_data(data)
                .custom_headers(List.of(new SmtpRequest.Header("Reply-To", replyTo)))
                .build();
        HttpEntity<SmtpRequest> request = new HttpEntity<>(smtpRequest);
        SmtpResponse response = restTemplate.postForObject(endpoint, request, SmtpResponse.class);
        // Can use this response to check if there are any error when request to SMTP server
    }

    @Override
    public Pair<Long, String> decodeKey(String key) {
        String decodedKey = new String(Base64.getUrlDecoder().decode(key.getBytes()));
        Long id;
        try {
            id = Long.valueOf(decodedKey.split(",")[0]);
        } catch (NumberFormatException e) {
            id = 0L;
        }
        String email = decodedKey.split(",")[1];
        return Pair.of(id, email);
    }

    private String generateConfirmUrl(Account account) {
        String key = generateEncodedKey(account);
        return String.format("%s/account/verified?key=%s", apiServer, key);
    }

    private String generateEncodedKey(Account account) {
        String key = String.format("%d,%s", account.getId(), account.getEmail());
        return Base64.getUrlEncoder().encodeToString(key.getBytes());
    }

}
