package com.shortly.Exceptions;

public class GetUrlNotFoundException extends RuntimeException{
    public GetUrlNotFoundException(String message){
        super(message);
    }
}
