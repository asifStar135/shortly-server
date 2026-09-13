package com.shortly.DTO.userDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPassword(
        @NotBlank(message = "Verification code cannot be empty.")
        String code,
        @NotBlank(message = "Email cannot be empty.")
        @Email(message = "Please enter a valid email")
        String email,
        @NotBlank(message = "New password cannot be empty.")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword
) {
}
