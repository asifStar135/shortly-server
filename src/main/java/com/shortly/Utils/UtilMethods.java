package com.shortly.Utils;

import com.shortly.DTO.Enums.DeviceType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
public class UtilMethods {

    private static final String SALT = "jd8h98nf9889398d_00d_19fneef";

    public static String getClientIp(HttpServletRequest request) {
        // Check standard proxy headers first
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
//            System.out.println("Proxy IP address : " + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // WebLogic fallback
//            System.out.println("WL Proxy IP address : " + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // Fallback to direct connection IP
//            System.out.println("Proxy IP address (Fallback to Remote address) : " + ip);
        }

        // If X-Forwarded-For contains multiple proxies, the first one is the real client
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    public static String encodeBase62(Long Id) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder res = new StringBuilder();

        while (Id > 0) {
            int index = (int) (Id % (62L));
            res.append(chars.charAt(index));
            Id /= 62;
        }
        // NOT NECESSARY, IT'S OKAY TO KEEP LESS CHARACTER THAN THE MAX - 7
//        while(res.length() < 7){
//            res.insert(0, "0");
//        }

        return res.toString();
    }

    public static String generateFingerprint(String ip, String userAgent) {
        try {
            String input = ip + "||" + userAgent + "||" + SALT;

            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(input.getBytes(StandardCharsets.UTF_8));

            // Instantly converts byte array to a clean 64-character hex string
            return HexFormat.of().formatHex(hash);

        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    public static DeviceType getDeviceType(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return DeviceType.OTHER;
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("android")) {
            return DeviceType.ANDROID;
        }

        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod")) {
            return DeviceType.IOS;
        }

        if (ua.contains("windows")) {
            return DeviceType.WINDOWS;
        }

        if (ua.contains("mac os") || ua.contains("macintosh")) {
            return DeviceType.MACOS;
        }

        if (ua.contains("linux")) {
            return DeviceType.LINUX;
        }

        return DeviceType.OTHER;
    }
}