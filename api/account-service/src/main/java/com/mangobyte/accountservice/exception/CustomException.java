package com.mangobyte.accountservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    @Getter
    private HttpStatus status;

    public CustomException() {
        super();
    }

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public CustomException(Exception exception) {
        super(exception.getMessage());
    }
}
