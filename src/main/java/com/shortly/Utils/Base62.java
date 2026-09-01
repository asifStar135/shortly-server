package com.shortly.Utils;

import org.springframework.stereotype.Component;

@Component
public class Base62 {
    public String encode(Long Id){
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder res = new StringBuilder();

        while (Id > 0){
            int index = (int)(Id%(62L));
            res.append(chars.charAt(index));
            Id /= 62;
        }

        return res.toString();
    }
}
