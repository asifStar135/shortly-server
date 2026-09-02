package com.shortly.Controllers;

import com.shortly.DTO.UpdateUserRequest;
import com.shortly.DTO.UserProfileWithData;
import com.shortly.Models.User;
import jakarta.validation.Valid;
import com.shortly.DTO.UserDataInput;
import com.shortly.Services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Value("${spring.env}")
    private String env;

    private final UserService userService;
    public UserController(UserService service){
        this.userService = service;
    }

    @PostMapping("/login")
    private ResponseEntity<UserDataInput> userLogin(@RequestBody UserDataInput userData){
        String token = userService.userLogin(userData);

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(env.equals("prod"))
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(60))
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userData);
    }

    @PostMapping("/register")
    private ResponseEntity<UserDataInput> registerUser(@Valid @RequestBody UserDataInput userData){
        String token = userService.registerUser(userData);

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(env.equals("prod"))
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(60))
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userData);
    }

    @GetMapping("/profile")
    private ResponseEntity<UserProfileWithData> getProfile(Authentication auth){
        String username = auth.getName();

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserProfile(username));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request, Authentication auth) {

        try {
            User updatedUser = userService.updateUser(auth.getName(), request);
            return ResponseEntity.ok(updatedUser);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteUser(Authentication auth) {
        try {
            String res = userService.deleteUser(auth.getName());
            return ResponseEntity.ok(res);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
