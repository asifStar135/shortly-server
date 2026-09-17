package com.shortly.Utils;

import com.shortly.Exceptions.UrlNotFoundException;
import com.shortly.Models.UrlMap;
import com.shortly.Models.UrlVisit;
import com.shortly.Repository.UrlRepo;
import com.shortly.Repository.UrlVisitRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class Analytics {
    @Autowired
    private UrlRepo urlRepo;

    @Autowired
    private UrlVisitRepo urlVisitRepo;

    @Autowired
    private GeoIPFinder IpFinder;

    @Async
    public void calculateAnalytics(String shortCode, String IPAdd, String userAgent, String clientIdCookie) {
        try {
            Thread.sleep(1000);

//            System.out.println("User Agent -> " + userAgent);
//            System.out.println("IP -> " + IPAdd);
//            System.out.println("Client cookies -> " + clientIdCookie);

            String clientIdFingerprint = UtilMethods.generateFingerprint(IPAdd, userAgent);
            UrlMap urlObj = urlRepo.findByShortCodeAndIsActive(shortCode, true)
                    .orElseThrow(() -> new UrlNotFoundException());

            UrlVisit urlVisit = null;

            if (clientIdCookie != null) {
                Optional<UrlVisit> urlVisitObj = urlVisitRepo
                        .findByUrlShortCodeAndClientCookie(shortCode, clientIdCookie);
                if (urlVisitObj.isPresent())
                    urlVisit = urlVisitObj.get();

            }
            if (urlVisit == null) {
                Optional<UrlVisit> urlVisitObj = urlVisitRepo
                        .findByUrlShortCodeAndClientHash(shortCode, clientIdFingerprint);
                if (urlVisitObj.isPresent())
                    urlVisit = urlVisitObj.get();
            }

            if (urlVisit != null) {
                urlVisit.setCount(urlVisit.getCount() + 1);
                urlVisit.setClientHash(clientIdFingerprint);
                //  increase today's visit only if last updated is today.
                if (urlVisit.getUpdatedAt().toLocalDate().isEqual(LocalDate.now())) {
                    urlVisit.setVisitToday(urlVisit.getVisitToday() + 1L);
                } else {
                    urlVisit.setVisitToday(1L);
                }
                urlVisitRepo.save(urlVisit);
                return;
            }

            UrlVisit newUrlVisitObj = new UrlVisit();
            newUrlVisitObj.setUrl(urlObj);
            newUrlVisitObj.setCount(1L);
            newUrlVisitObj.setClientHash(clientIdFingerprint);
            newUrlVisitObj.setClientCookie(clientIdCookie);
            String country = IpFinder.getCountry(IPAdd);
            String city = IpFinder.getCity(IPAdd);
            newUrlVisitObj.setCountry(country);
            newUrlVisitObj.setCity(city);
            newUrlVisitObj.setDevice(UtilMethods.getDeviceType(userAgent));
            urlVisitRepo.save(newUrlVisitObj);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
