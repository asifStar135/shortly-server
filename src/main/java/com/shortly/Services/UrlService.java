package com.shortly.Services;

import com.shortly.DTO.CreateUrlRequest;
import com.shortly.DTO.CreateUrlResponse;
import com.shortly.DTO.EditAction;
import com.shortly.DTO.EditUrlRequest;
import com.shortly.Exceptions.GetUrlNotFoundException;
import com.shortly.Exceptions.UrlNotFoundException;
import com.shortly.Models.UrlMap;
import com.shortly.Models.User;
import com.shortly.Repository.UrlRepo;
import com.shortly.Repository.UserRepo;
import com.shortly.Utils.Analytics;
import com.shortly.Utils.Base62;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UrlService {

    private final UrlRepo urlRepo;
    private final Analytics analyticObject;
    private final UserRepo userRepo;
    private final Base62 base62Encoder;

    public UrlService(UrlRepo repo, UserRepo userRepo, Analytics analytics, Base62 base62){
        this.analyticObject = analytics;
        this.urlRepo = repo;
        this.userRepo = userRepo;
        this.base62Encoder = base62;
    }

    public String getUrl(String shortCode) {
        UrlMap urlDetails = urlRepo.findUrlData(shortCode, true, new Date())
                .orElseThrow(() -> new GetUrlNotFoundException("Wrong short code"));

        try{
            analyticObject.calculateAnalytics(urlDetails.getId());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return urlDetails.getLongUrl();
    }

    public CreateUrlResponse createUrl(CreateUrlRequest urlData, String username) {
        User loggedInUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UrlMap newUrl = new UrlMap();
        newUrl.setLongUrl(urlData.longUrl());
        newUrl.setExpiresAt(urlData.expires());
        newUrl.setTitle(urlData.title());
        newUrl.setUser(loggedInUser);

        newUrl = urlRepo.save(newUrl);
        String shortCode = base62Encoder.encode(newUrl.getId());
        newUrl.setShortCode(shortCode);
        urlRepo.save(newUrl);

        return new CreateUrlResponse(newUrl.getId(), shortCode, newUrl.getLongUrl(), true,
                newUrl.getExpiresAt(), newUrl.getUpdatedAt(), newUrl.getCreatedAt(), loggedInUser.getUserId());
    }

    public void deleteUrl(Long id, String username) {
        urlRepo.deleteByIdAndUserUsername(id, username);
    }

    public List<UrlMap> getAllUrls(String userName) {
        return urlRepo.findByUserUsername(userName);
    }

    public UrlMap getUrlById(Long id,String userName) {

        return urlRepo.findByIdAndUserUsername(id,userName).orElseThrow(() -> new UrlNotFoundException("Url not found"));
    }

    public UrlMap editUrlDetails(Long id, EditUrlRequest request, String username) {
        UrlMap urlObj = urlRepo.findByIdAndUserUsername(id, username).orElseThrow(() -> new UrlNotFoundException("Url not found"));

        switch (request.editAction()) {
            case ENABLE -> urlObj.setActive(true);
            case DISABLE -> urlObj.setActive(false);
            case TITLE -> urlObj.setTitle(request.title());
            case LONG_URL -> urlObj.setLongUrl(request.longUrl());
            case EXPIRES -> urlObj.setExpiresAt(request.expires());
        }

        return urlRepo.save(urlObj);
    }
}