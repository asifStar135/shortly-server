package com.shortly.Utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public final class ResponseHandler {

    public static ResponseEntity<ResponseObject> handleSuccess(int statusCode, Object data, String message){
        return ResponseEntity.ok(new ResponseObject(statusCode,message, data, null));
    }

    // Method overloading with setting cookie for login, register route.
    public static ResponseEntity<ResponseObject> handleSuccess(int statusCode, Object data, String message, String token){
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(60))
                .build();

        return ResponseEntity.status(200)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ResponseObject(statusCode,message, data, null));
    }

    public static ResponseEntity<ResponseObject> handleError(int statusCode, String errorCode, String message){
        return ResponseEntity.status(statusCode).body(new ResponseObject(statusCode,
                message == null ?  ErrorCodes.MESSAGES.get(errorCode) : message, null, errorCode));
    }

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<ResponseObject> handleServerError(){
        return ResponseEntity.internalServerError()
                .body(new ResponseObject(500,ErrorCodes.MESSAGES
                        .get("INTERNAL_SERVER_ERROR"), null, "INTERNAL_SERVER_ERROR"));
    }

    public static ResponseEntity<ResponseObject> handleRedirect(String url){
        try {
            URI targetUri = new URI(url);

            return ResponseEntity.status(302).location(targetUri).build();
        } catch (URISyntaxException ex){
            return handleError(404, "No valid URL found", null);
        }
    }
}
