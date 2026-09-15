package com.shortly.Utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CodeGenerator {

    private final static SecureRandom secureRandom = new SecureRandom();

    public static String generate6DigitCode() {
        int code = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(code);
    }
}