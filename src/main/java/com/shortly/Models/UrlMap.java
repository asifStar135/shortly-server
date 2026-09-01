package com.shortly.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.util.Date;

@Entity
@Table(name = "url_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @NonNull
    @Column(unique = true)
    private String shortCode;

    @NonNull
    @Column(length = 2000)
    private String longUrl;

    private Long visit = 0L;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Date createdAt = new Date();
    private Date updatedAt = new Date();
    private Date expiresAt;
}
