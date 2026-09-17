package com.shortly.Controllers;

import com.shortly.DTO.userDTOs.*;
import com.shortly.Models.User;
import com.shortly.Utils.ResponseHandler;
import com.shortly.Utils.ResponseObject;
import jakarta.validation.Valid;
import com.shortly.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

    @PostMapping("/login")
    private ResponseEntity<ResponseObject> userLogin(@Valid @RequestBody UserDataInput userData) {
        String token = userService.userLogin(userData);

        return ResponseHandler.handleSuccess(200, null, "Logged in successfully.", token);
    }

    @PostMapping("/register")
    private ResponseEntity<ResponseObject> registerUser(@Valid @RequestBody UserDataInput userData) {
        String token = userService.registerUser(userData);

        return ResponseHandler.handleSuccess(200, null, "Signed up successfully.", token);
    }

    @GetMapping("/profile")
    private ResponseEntity<ResponseObject> getProfile(Authentication auth) {
        String username = auth.getName();

        UserProfileWithData data = userService.getUserProfile(username);
        return ResponseHandler.handleSuccess(200, data, "Profile data fetched.");
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseObject> updateUser(@RequestBody UpdateUserRequest request, Authentication auth) {
        User updatedUser = userService.updateUser(auth.getName(), request);
        return ResponseHandler.handleSuccess(200, updatedUser, "User details updated");
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ResponseObject> deleteUser(Authentication auth) {
        userService.deleteUser(auth.getName());

        return ResponseHandler.handleSuccess(200, null, "User deleted successfully");
    }

    // Step A: Request Code with new password & email
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseObject> forgotPassword(@RequestBody @Valid ForgotPassword request) {
        userService.initiatePasswordReset(request.email());

        return ResponseHandler.handleSuccess(200, null, "A verification code has been sent to your email");
    }

    // Step B: Submit Code
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseObject> verifyCode(@RequestBody @Valid ResetPassword request) {
        userService.resetPassword(request.email(), request.code(), request.newPassword());
        return ResponseHandler.handleSuccess(200, null, "Password has been reset successfully");
    }
}
