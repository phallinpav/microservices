package com.mangobyte.accountservice;

public class ErrorMessageUtils {

    public final static String EMPTY_BODY = "Required request body is missing";
    public final static String REQUIRE_USER_OR_EMAIL = "username or email is required";
    public final static String REQUEST_NOT_FOUND_MESSAGE = "request not found";
    public final static String FRIEND_NOT_FOUND_MESSAGE = "friend not found";
    public final static String ALREADY_REQUESTED_MESSAGE = "already requested";
    public final static String BLOCKING_THIS_ACCOUNT = "blocking this account";
    public final static String BLOCKED_FROM_THIS_ACCOUNT = "blocked from this account";
    public static final String INVALID_SELF_REQUESTING = "invalid self requesting";
    public final static String ALREADY_FRIEND_MESSAGE = "already friend";
    public final static String REQUESTING_BAD_STATUS = "invalid status";
    public final static String INTERNAL_ERROR = "internal error";
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
