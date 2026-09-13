package com.shortly.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.util.Date;

public record CreateUrlRequest(
<<<<<<< Updated upstream:src/main/java/com/shortly/DTO/CreateUrlRequest.java
    @NotBlank
    String title,
    Date expires,
    @NotBlank
    @URL
    String longUrl
)
{ }
=======
        @NotBlank(message = "Please enter a title")
        String title,
        @Future(message = "Expiry cannot be a past date")
        Date expires,
        @NotBlank
        @URL(message = "Please enter a valid URL")
        String longUrl
) {
}
>>>>>>> Stashed changes:src/main/java/com/shortly/DTO/UrlDTOs/CreateUrlRequest.java
