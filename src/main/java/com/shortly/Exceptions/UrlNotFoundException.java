package com.shortly.Exceptions;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String shortCode){
        super("No Url found with : " + shortCode);
    }
}