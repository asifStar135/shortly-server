package com.shortly.Repository;

import com.shortly.DTO.UrlDTOs.*;
import jakarta.transaction.Transactional;
import com.shortly.Models.UrlMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepo extends JpaRepository<UrlMap, Long> {

    @Query("""
            select url from UrlMap url where url.isActive = :isActive and url.shortCode = :shortCode and (url.expiresAt is null OR url.expiresAt > :currentDate)
            """)
    Optional<UrlMap> findUrlData(@Param("shortCode") String shortCode, @Param("isActive") boolean isActive, @Param("currentDate") Date currentDate);

    Optional<UrlMap> findByShortCodeAndIsActive(String shortCode, boolean isActive);

    Optional<UrlMap> findById(Long id);

    @Query("""
                SELECT new com.shortly.DTO.UrlDTOs.UrlListItem(
                    u.id,
                    u.title,
                    u.shortCode,
                    u.longUrl,
                    u.isActive,
                    COALESCE(SUM(uv.count), 0),
                    u.createdAt,
                    u.expiresAt
                )
                FROM UrlMap u
                LEFT JOIN UrlVisit uv ON uv.url.id = u.id
                WHERE u.user.username = :username
                GROUP BY
                    u.id,
                    u.title,
                    u.shortCode,
                    u.longUrl,
                    u.isActive,
                    u.createdAt,
                    u.expiresAt
                ORDER BY u.createdAt DESC
            """)
    List<UrlListItem> findUrlList(@Param("username") String username);

    @Query("""
            SELECT new com.shortly.DTO.UrlDTOs.UrlItemBase(
                u.id,
                u.title,
                u.shortCode,
                u.longUrl,
                u.isActive,
                u.expiresAt,
                u.createdAt,
                u.updatedAt,
                COALESCE(SUM(uv.count), 0),
                COUNT(uv),
                COALESCE(SUM(
                    CASE WHEN uv.updatedAt >= CURRENT_DATE THEN uv.visitToday ELSE 0 END
                ), 0)
            )
            FROM UrlVisit uv
            RIGHT JOIN uv.url u
            JOIN u.user usr
            WHERE u.id = :urlId
              AND usr.username = :username
            GROUP BY
                u.id, u.title, u.shortCode, u.longUrl,
                u.isActive, u.expiresAt, u.createdAt, u.updatedAt
            """)
    UrlItemBase getUrlItemWithData(
            @Param("urlId") Long urlId,
            @Param("username") String username
    );

    @Query("""
                SELECT new com.shortly.DTO.UrlDTOs.VisitByCity(
                        uv.city,
                        uv.country,
                        SUM(uv.count)
                    )
                    FROM UrlVisit uv
                    WHERE uv.url.id = :urlId
                    GROUP BY uv.city, uv.country
                    ORDER BY SUM(uv.count) DESC
            """)
    List<VisitByCity> getVisitsByCity(@Param("urlId") Long urlId);

    @Query("""
                SELECT new com.shortly.DTO.UrlDTOs.VisitByDevice(
                        uv.device,
                        SUM(uv.count)
                    )
                    FROM UrlVisit uv
                    WHERE uv.url.id = :urlId
                    GROUP BY uv.device
                    ORDER BY SUM(uv.count) DESC
            """)
    List<VisitByDevice> getVisitsByDevice(@Param("urlId") Long urlId);

    Optional<UrlMap> findByIdAndUserUsername(Long id, String userName);

    @Transactional
    void deleteByIdAndUserUsername(Long id, String username);
}
