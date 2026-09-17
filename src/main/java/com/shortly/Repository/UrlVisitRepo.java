package com.shortly.Repository;

import com.shortly.Models.UrlVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlVisitRepo extends JpaRepository<UrlVisit, Long> {

    Optional<UrlVisit> findByUrlShortCodeAndClientCookie(String shortCode, String clientIdCookie);

    Optional<UrlVisit> findByUrlShortCodeAndClientHash(String shortCode, String clientHash);
}
