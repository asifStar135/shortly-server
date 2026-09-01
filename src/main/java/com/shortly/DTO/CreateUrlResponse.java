package com.shortly.DTO;

import java.util.Date;

public record CreateUrlResponse(
        Long id,
        String shortCode,
        String longUrl,
        boolean is_active,
        Date expires_at,
        Date updated_at,
        Date created_at,
        Long user_id
) {
}
