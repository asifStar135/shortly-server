package com.shortly.DTO.userDTOs;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
    @NotBlank
    boolean isUsername,
    String username,
    String email
) {
}
