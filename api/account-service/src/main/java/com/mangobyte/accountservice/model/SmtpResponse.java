package com.mangobyte.accountservice.model;

import lombok.Data;

import java.util.List;

@Data
public class SmtpResponse {
    String request_id;
    SmtpData data;

    @Data
    public static class SmtpData {
        String succeeded;
        String failed;
        List<Object> failures;
        String email_id;
    }
}
