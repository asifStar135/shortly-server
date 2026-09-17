package com.shortly.DTO.UrlDTOs;

import java.util.Date;

public record UrlListItem(
        Long id,
        String title,
        String shortCode,
        String longUrl,
        boolean isActive,
        Long total_visit,
        Date createdAt,
        Date expiresAt
) {
}
