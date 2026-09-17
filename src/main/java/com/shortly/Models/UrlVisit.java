package com.shortly.Models;

import com.shortly.DTO.Enums.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "url_visit")
public class UrlVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private UrlMap url;

    private Long count = 0L;
    //    Can't be unique for similar devices in same network
//    @Column(unique = true)
    private String clientHash;

    //    It can't be unique because a device can access different links
//    @Column(unique = true)
    private String clientCookie;

    private String country;

    private String city;

    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Long visitToday = 1L;

    @Enumerated(EnumType.STRING)
    private DeviceType device;
}
