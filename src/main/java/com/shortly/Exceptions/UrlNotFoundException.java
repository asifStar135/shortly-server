package com.shortly.Exceptions;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException() {
        super("No Url found !");
    }
}