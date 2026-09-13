package com.shortly.Utils;

import java.util.Map;

public final class ErrorCodes {

    private ErrorCodes() {
    }

    public static final String URL_NOT_FOUND = "URL_NOT_FOUND";
    public static final String URL_EXPIRED = "URL_EXPIRED";
    public static final String URL_DISABLED = "URL_DISABLED";
    public static final String INVALID_URL = "INVALID_URL";

    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USERNAME_EXISTS = "USERNAME_EXISTS";
    public static final String EMAIL_EXISTS = "EMAIL_EXISTS";
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String INVALID_CODE = "INVALID_CODE";
    public static final String TOO_MANY_ATTEMPTS = "TOO_MANY_ATTEMPTS";

    public static final String INVALID_REQUEST = "INVALID_REQUEST";
    public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    public static final Map<String, String> MESSAGES = Map.ofEntries(
            Map.entry(URL_NOT_FOUND, "Short URL not found!"),
            Map.entry(URL_EXPIRED, "Short URL has expired!"),
            Map.entry(URL_DISABLED, "Short URL is disabled!"),
            Map.entry(INVALID_URL, "Invalid URL provided!"),

            Map.entry(USER_NOT_FOUND, "User not found!"),
            Map.entry(USERNAME_EXISTS, "Username already exists!"),
            Map.entry(EMAIL_EXISTS, "Email already exists!"),
            Map.entry(INVALID_CREDENTIALS, "Wrong credentials provided !"),
            Map.entry(UNAUTHORIZED, "Authentication required!"),
            Map.entry(FORBIDDEN, "You do not have permission to perform this action!"),
            Map.entry(INVALID_CODE, "Invalid code provided!"),
            Map.entry(TOO_MANY_ATTEMPTS, "Too many attempts!"),

            Map.entry(INVALID_REQUEST, "Please enter valid inputs !"),
            Map.entry(VALIDATION_FAILED, "Request validation failed!"),
            Map.entry(RESOURCE_NOT_FOUND, "Requested resource not found!"),
            Map.entry(INTERNAL_SERVER_ERROR, "Something went wrong, please try agai!n")
    );
}