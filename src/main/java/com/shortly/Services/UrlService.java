package com.shortly.Services;

import com.shortly.DTO.UrlDTOs.*;
import com.shortly.Exceptions.GetUrlNotFoundException;
import com.shortly.Exceptions.UrlNotFoundException;
import com.shortly.Models.UrlMap;
import com.shortly.Models.User;
import com.shortly.Repository.UrlRepo;
import com.shortly.Repository.UserRepo;
import com.shortly.Utils.UtilMethods;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UrlService {

    private final UrlRepo urlRepo;
    private final UserRepo userRepo;

    public UrlService(UrlRepo repo, UserRepo userRepo) {
        this.urlRepo = repo;
        this.userRepo = userRepo;
    }

    public String getUrl(String shortCode) {
        UrlMap urlDetails = urlRepo.findUrlData(shortCode, true, new Date())
                .orElseThrow(() -> new GetUrlNotFoundException("Wrong short code"));

        return urlDetails.getLongUrl();
    }

    public boolean createUrl(CreateUrlRequest urlData, String username) {
        User loggedInUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UrlMap newUrl = new UrlMap();
        newUrl.setLongUrl(urlData.longUrl());
        newUrl.setExpiresAt(urlData.expires());
        newUrl.setTitle(urlData.title());
        newUrl.setUser(loggedInUser);

        newUrl = urlRepo.save(newUrl);
        String shortCode = UtilMethods.encodeBase62(newUrl.getId());
        newUrl.setShortCode(shortCode);
        urlRepo.save(newUrl);

        return true;
    }

    public void deleteUrl(Long id, String username) {
        urlRepo.deleteByIdAndUserUsername(id, username);
    }

    public List<UrlListItem> getAllUrls(String username) {
        return urlRepo.findUrlList(username);
    }

    public UrlItemResponse getUrlDataById(Long id, String username) {
        UrlItemBase urlItemBase = urlRepo.getUrlItemWithData(id, username);

        List<VisitByCity> city_visits = urlRepo.getVisitsByCity(id);
        List<VisitByDevice> device_visits = urlRepo.getVisitsByDevice(id);

        return new UrlItemResponse(
                urlItemBase.id(), urlItemBase.title(), urlItemBase.shortCode(), urlItemBase.longUrl(),
                urlItemBase.isActive(), urlItemBase.expiresAt(), urlItemBase.createdAt(), urlItemBase.updatedAt(),
                urlItemBase.total_visit(), urlItemBase.unique_visit(), urlItemBase.today_visit(),
                city_visits, device_visits
        );
    }

    public boolean editUrlDetails(Long id, EditUrlRequest request, String username) {
        UrlMap urlObj = urlRepo.findByIdAndUserUsername(id, username).orElseThrow(() -> new UrlNotFoundException());

        switch (request.editAction()) {
            case ENABLE -> urlObj.setActive(true);
            case DISABLE -> urlObj.setActive(false);
            case TITLE -> urlObj.setTitle(request.title());
            case LONG_URL -> urlObj.setLongUrl(request.longUrl());
            case EXPIRES -> urlObj.setExpiresAt(request.expires());
        }

        urlRepo.save(urlObj);
        return true;
    }
}