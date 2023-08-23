package com.mangobyte.accountservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SmtpRequest {
    final String api_key;
    final List<String> to;
    final String sender;
    final String template_id;
    final Map<String, String> template_data;
    final List<Header> custom_headers;
    final List<Object> attachments;

    @Data
    public static class Header {
        final String header;
        final String value;
    }
}
