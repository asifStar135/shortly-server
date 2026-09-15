package com.shortly.Exceptions;

import com.shortly.Utils.ErrorCodes;
import com.shortly.Utils.ResponseHandler;
import com.shortly.Utils.ResponseObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.client_url}")
    private String client_url;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidation(MethodArgumentNotValidException ex){
        String msg = ex.getBindingResult().getFieldErrors()
                .get(0).getDefaultMessage();

        return ResponseHandler.handleError(400, ErrorCodes.INVALID_REQUEST, msg);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseObject> handleBadRequest(BadRequestException ex){

        return ResponseHandler.handleError(400, ex.getMessage(), null);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ResponseObject> handleUrlNotFound(){
        return ResponseHandler.handleError(404, ErrorCodes.URL_NOT_FOUND, null);
    }

    @ExceptionHandler(GetUrlNotFoundException.class)
    public ResponseEntity<ResponseObject> handleGetUrlNotFound(){
        return ResponseHandler.handleRedirect(client_url + "not-found");
    }

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<ResponseObject> handleAuthError(){
        return ResponseHandler.handleError(401, ErrorCodes.UNAUTHORIZED, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseObject> handleWrongCredentials(){
        return ResponseHandler.handleError(401, ErrorCodes.INVALID_CREDENTIALS, null);
    }
}