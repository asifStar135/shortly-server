package com.shortly.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record EditUrlRequest(
        @NotBlank
        EditAction editAction,
        String title,
        String longUrl,
        Date expires
) {
}
