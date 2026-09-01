package com.shortly.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDataInput(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @Email
        String email
) {
}
