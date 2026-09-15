package com.shortly.DTO.userDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPassword(
        @NotBlank(message = "Email is required")
        @Email(message = "Please enter a valid email")
        String email
) {
}
