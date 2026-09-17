package com.shortly.DTO.UrlDTOs;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

public record UrlItemResponse(
        @NotBlank
        Long id,
        @NotBlank
        String title,
        @NotBlank
        String shortCode,
        @NotBlank
        String longUrl,
        @NotBlank
        boolean isActive,
        @NotBlank
        Date expiresAt,
        Date createdAt,
        Date updatedAt,
        Long total_visit,
        Long unique_visit,
        Long today_visit,
        List<VisitByCity> city_visits,
        List<VisitByDevice> device_visits
) {
}