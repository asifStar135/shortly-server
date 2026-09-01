package com.shortly.Repository;

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
public interface UrlRepo extends JpaRepository<UrlMap, Integer> {

    @Query("select url from UrlMap url where url.isActive = :is_active and url.shortCode = :shortCode and (url.expiresAt is null OR url.expiresAt > :currentDate)")
    Optional<UrlMap> findUrlData(@Param("shortCode") String shortUrl,@Param("is_active") boolean is_active,@Param("currentDate") Date currentDate);

    Optional<UrlMap> findByShortCodeAndIsActive(String shortCode, boolean isActive);

    Optional<UrlMap> findById(Long id);

    @Transactional
    void deleteByShortCode(String shortCode);

    List<UrlMap> findByUserUsername(String userName);

    Optional<UrlMap> findByIdAndUserUsername(Integer id, String userName);

    Optional<UrlMap> findByShortCodeAndIsActiveAndUserUsername(String shortCode, boolean b, String username);

    void deleteByShortCodeAndUserUsername(String shortCode, String username);

    @Transactional
    void deleteByIdAndUserUsername(Long id, String username);
}
