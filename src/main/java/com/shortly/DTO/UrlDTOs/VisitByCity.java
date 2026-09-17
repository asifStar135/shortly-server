package com.shortly.DTO.UrlDTOs;

public record VisitByCity(
        String city,
        String country,
        Long count
) {
}
