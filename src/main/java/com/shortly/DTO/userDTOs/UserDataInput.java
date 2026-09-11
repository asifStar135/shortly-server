package com.shortly.DTO.userDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserDataInput(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        @Length(min = 8, message = "Password must be 8 characters long !")
        String password,
        @Email(message = "Enter a valid email")
        String email
) {
}
