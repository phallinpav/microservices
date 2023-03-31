package com.mangobyte.accountservice;

public class ErrorMessageUtils {

    public final static String EMPTY_BODY = "Required request body is missing";
    public final static String REQUIRE_USER_OR_EMAIL = "username or email is required";

    private final static String NOTNULL_MESSAGE = "must not be null";
    private final static String EMAIL_MESSAGE = "must be a well-formed email address";
    private final static String SIZE_MESSAGE = "size must be between %s and %s";

    public static String fieldNullError(String field) {
        return fieldError(field, NOTNULL_MESSAGE);
    }

    public static String fieldEmailError(String field) {
        return fieldError(field, EMAIL_MESSAGE);
    }

    public static String fieldSizeError(String field, int min, int max) {
        return fieldError(field, String.format(SIZE_MESSAGE, min, max));
    }

    private static String fieldError(String field, String message) {
        return String.format("%s : %s", field, message);
    }
}
