package com.shortly.Controllers;

import com.shortly.DTO.UrlDTOs.UrlListItem;
import com.shortly.Utils.Analytics;
import com.shortly.Utils.ResponseHandler;
import com.shortly.Utils.ResponseObject;
import com.shortly.Utils.UtilMethods;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.shortly.DTO.UrlDTOs.CreateUrlRequest;
import com.shortly.DTO.UrlDTOs.UrlItemResponse;
import com.shortly.DTO.UrlDTOs.EditUrlRequest;
import com.shortly.Services.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;
    private final Analytics analyticObject;

    public UrlController(UrlService service, Analytics analytics) {
        this.analyticObject = analytics;
        this.urlService = service;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> checkAPI() {
        System.out.println("API status checked ✅");
        return ResponseHandler.handleSuccess(200, null, "Shortly API is running...✅🚀");
    }

    @GetMapping("/get/{shortCode}")
    ResponseEntity<ResponseObject> getUrl(@PathVariable String shortCode,
                                          @CookieValue(value = "client_id", required = false) String clientIdCookie,
                                          HttpServletRequest request) {
        // Find the mapped long URL
        String longUrl = urlService.getUrl(shortCode);

        // Get the request params
        String userAgent = request.getHeader("User-Agent");
        String IPAdd = UtilMethods.getClientIp(request);

        // Generate cookie if not exist
        String cookieValue = null;
        //  in case of empty cookies, generate a new cookie and set to response.
        if (clientIdCookie == null) {
            cookieValue = UUID.randomUUID().toString();
        }

        // async calculations
        analyticObject.calculateAnalytics(shortCode, IPAdd, userAgent, cookieValue);

        return ResponseHandler.handleRedirect(longUrl, cookieValue);
    }

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createUrl(@Valid @RequestBody CreateUrlRequest urlData, Authentication auth) {
        urlService.createUrl(urlData, auth.getName());

        return ResponseHandler.handleSuccess(200, true, "Short URL created successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllUserURI(Authentication auth) {
        List<UrlListItem> urlList = urlService.getAllUrls(auth.getName());

        return ResponseHandler.handleSuccess(200, urlList, "Created URLs fetched");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUserUri(@PathVariable(value = "id") Long id, Authentication auth) {
        UrlItemResponse urlDetails = urlService.getUrlDataById(id, auth.getName());

        return ResponseHandler.handleSuccess(200, urlDetails, "URL details fetched.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> editUrlDetails(@PathVariable(value = "id") Long id,                                                       @RequestBody EditUrlRequest request, Authentication auth) {
        urlService.editUrlDetails(id, request, auth.getName());
        return ResponseHandler.handleSuccess(200, null, "URL details updated");
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUrl(@PathVariable Long id, Authentication auth) {
        urlService.deleteUrl(id, auth.getName());
        return ResponseHandler.handleSuccess(204, null, "URL has been deleted");
    }
}