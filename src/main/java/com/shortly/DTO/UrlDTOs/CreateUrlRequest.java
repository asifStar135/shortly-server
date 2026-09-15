package com.shortly.DTO.UrlDTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.util.Date;

public record CreateUrlRequest(
        @NotBlank(message = "Please enter a title")
        String title,
        @Future(message = "Expiry cannot be a past date")
        Date expires,
        @NotBlank
        @URL(message = "Please enter a valid URL")
        String longUrl
) {
}