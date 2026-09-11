package com.shortly.Exceptions;

public class UserAuthException extends RuntimeException {
    public UserAuthException(String message) {
        super(message);
    }
}
