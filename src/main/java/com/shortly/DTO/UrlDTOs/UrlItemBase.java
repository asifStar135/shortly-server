package com.shortly.DTO.UrlDTOs;

import java.util.Date;

public record UrlItemBase(
        Long id,
        String title,
        String shortCode,
        String longUrl,
        boolean isActive,
        Date expiresAt,
        Date createdAt,
        Date updatedAt,
        Long total_visit,
        Long unique_visit,
        Long today_visit
) {
}