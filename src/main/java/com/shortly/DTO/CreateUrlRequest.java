package com.shortly.DTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.util.Date;

public record CreateUrlRequest(
    @NotBlank
    String title,
    Date expires,
    @NotBlank
    @URL
    String longUrl
)
{ }
