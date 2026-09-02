package com.shortly.Utils;

import com.shortly.Exceptions.UrlNotFoundException;
import com.shortly.Models.UrlMap;
import com.shortly.Repository.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Analytics {
    @Autowired
    private UrlRepo urlRepo;

    @Async
    public void calculateAnalytics(Long urlId ){
        try{

            UrlMap urlObj = urlRepo.findById(urlId).orElseThrow(() -> new UrlNotFoundException(urlId+""));

//            urlObj.setVisit(urlObj.getVisit()+1);
            urlRepo.save(urlObj);
//            System.out.println(urlObj.getVisit());
            // OTHER ANALYTICS CALCULATION FOR HITTING A URL

//            System.out.println(new Date(System.currentTimeMillis()).getTime() + "-> above sleep");
            Thread.sleep(3000);
//            System.out.println(new Date(System.currentTimeMillis()).getTime() + "-> below sleep");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
