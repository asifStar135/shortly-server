package com.shortly.Controllers;

import com.shortly.Utils.ResponseHandler;
import com.shortly.Utils.ResponseObject;
import jakarta.validation.Valid;
import com.shortly.DTO.UrlDTOs.CreateUrlRequest;
import com.shortly.DTO.UrlDTOs.CreateUrlResponse;
import com.shortly.DTO.UrlDTOs.EditUrlRequest;
import com.shortly.Models.UrlMap;
import com.shortly.Services.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService service) {
        this.urlService = service;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> checkAPI(){
        System.out.println("API status checked ✅");
        return ResponseHandler.handleSuccess(200, null, "Shortly API is running...✅🚀");
    }

    @GetMapping("/get/{shortCode}")
    ResponseEntity<ResponseObject> getUrl(@PathVariable String shortCode){
        String longUrl = urlService.getUrl(shortCode);

        return ResponseHandler.handleRedirect(longUrl);
    }

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createUrl(@Valid @RequestBody CreateUrlRequest urlData, Authentication auth){
        CreateUrlResponse urlDetails = urlService.createUrl(urlData, auth.getName());

        return ResponseHandler.handleSuccess(200, urlDetails, "Short URL created successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllUserURI(Authentication auth){
        List<UrlMap> urlList = urlService.getAllUrls(auth.getName());

        return ResponseHandler.handleSuccess(200, urlList, "Created URLs fetched");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUserUri(@PathVariable(value = "id") Long id, Authentication auth){
        UrlMap urlDetails = urlService.getUrlById(id, auth.getName());

        return ResponseHandler.handleSuccess(200, urlDetails, "URL details fetched.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> editUrlDetails(@PathVariable(value = "id") Long id,
                                                 @RequestBody EditUrlRequest request, Authentication auth){
        UrlMap updatedUrl = urlService.editUrlDetails(id, request, auth.getName());

        return ResponseHandler.handleSuccess(200, updatedUrl, "URL details updated");
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUrl(@PathVariable Long id, Authentication auth){
        urlService.deleteUrl(id, auth.getName());
        return ResponseHandler.handleSuccess(204, null, "URL has been deleted");
    }
}