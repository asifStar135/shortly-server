package com.shortly.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserDataInput(
        @NotBlank
        String username,
<<<<<<< Updated upstream:src/main/java/com/shortly/DTO/UserDataInput.java
        @NotBlank
=======
        @NotBlank(message = "Password is required")
        @Length(min = 8, message = "Password must be 8 characters long !")
>>>>>>> Stashed changes:src/main/java/com/shortly/DTO/userDTOs/UserDataInput.java
        String password,
        @Email
        String email
) {
}
