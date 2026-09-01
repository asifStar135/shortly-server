package com.shortly.DTO;


import java.util.Date;

public record UserProfileWithData(
        Long userId,
        String username,
        String email,
        Date createdAt,
        Date updatedAt,
        Long totalUrls,
        Long totalVisits,
        Long activeUrls
) {
}
