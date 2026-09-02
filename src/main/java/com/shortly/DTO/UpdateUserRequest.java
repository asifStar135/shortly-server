package com.shortly.DTO;

public record UpdateUserRequest(
    String username,
    String email
) {
}
