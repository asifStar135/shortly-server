package com.shortly.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record EditUrlRequest(
        EditAction editAction,
        String title,
        String longUrl,
        @Future(message = "Expires cannot be a past date")
        Date expires
) {
}
