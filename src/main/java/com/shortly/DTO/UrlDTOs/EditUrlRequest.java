package com.shortly.DTO.UrlDTOs;

import com.shortly.DTO.Enums.EditAction;
import jakarta.validation.constraints.Future;

import java.util.Date;

public record EditUrlRequest(
        EditAction editAction,
        String title,
        String longUrl,
        @Future(message = "Expires cannot be a past date")
        Date expires
) {
}
