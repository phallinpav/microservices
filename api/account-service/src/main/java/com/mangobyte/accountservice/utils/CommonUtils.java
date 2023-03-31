package com.mangobyte.accountservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class CommonUtils {
    private final static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        objectMapper.setDateFormat(simpleDateFormat);
    }

    public static String encodedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean isMatch(String password, String encoded) {
        return bCryptPasswordEncoder.matches(password, encoded);
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
