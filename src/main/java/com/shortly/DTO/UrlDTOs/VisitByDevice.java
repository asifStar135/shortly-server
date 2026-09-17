package com.shortly.DTO.UrlDTOs;

import com.shortly.DTO.Enums.DeviceType;

public record VisitByDevice(
        DeviceType deviceType,
        Long count
) {
}
