package com.shortly.Exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.client_url}")
    private String client_url;

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<String> handleUrlNotFound(UrlNotFoundException ex){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(GetUrlNotFoundException.class)
    public ResponseEntity<?> handleGetUrlNotFound() throws Exception{

        return ResponseEntity.
                status(HttpStatusCode.valueOf(302)).
                location(new URI(client_url + "not-found")).build();
    }
}
