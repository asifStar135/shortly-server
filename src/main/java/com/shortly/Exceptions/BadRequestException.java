package com.shortly.Exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String errorCode){
        super(errorCode);
    }
}
