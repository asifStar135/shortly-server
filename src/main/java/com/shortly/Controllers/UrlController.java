package com.shortly.Controllers;

import jakarta.validation.Valid;
import com.shortly.DTO.CreateUrlRequest;
import com.shortly.DTO.CreateUrlResponse;
import com.shortly.DTO.EditAction;
import com.shortly.DTO.EditUrlRequest;
import com.shortly.Models.UrlMap;
import com.shortly.Services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

<<<<<<< Updated upstream
import java.net.URI;
import java.net.URISyntaxException;
=======
>>>>>>> Stashed changes
import java.util.List;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService service) {
        this.urlService = service;
    }

    @GetMapping("/")
<<<<<<< Updated upstream
    public ResponseEntity<String> checkAPI(){
=======
    public ResponseEntity<ResponseObject> checkAPI() {
>>>>>>> Stashed changes
        System.out.println("API status checked ✅");
        return ResponseEntity.ok("Shortly API is running...✅🚀");
    }

    @GetMapping("/get/{shortCode}")
<<<<<<< Updated upstream
    ResponseEntity<?> getUrl(@PathVariable String shortCode) throws URISyntaxException{
=======
    ResponseEntity<ResponseObject> getUrl(@PathVariable String shortCode) {
>>>>>>> Stashed changes
        String longUrl = urlService.getUrl(shortCode);

        URI targetUri = new URI(longUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(targetUri)
                .build();
    }

    @PostMapping("/create")
<<<<<<< Updated upstream
    ResponseEntity<CreateUrlResponse> createUrl(@Valid @RequestBody CreateUrlRequest urlData,
                                                Authentication auth){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(urlService.createUrl(urlData, auth.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UrlMap>> getAllUserURI(Authentication auth){
        return ResponseEntity.ok(urlService.getAllUrls(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlMap> getUserUri(@PathVariable(value = "id") Long id, Authentication auth){
        return ResponseEntity.ok(urlService.getUrlById(id, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UrlMap> editUrlDetails(@PathVariable(value = "id") Long id,
                                                 @RequestBody EditUrlRequest request, Authentication auth){
        return ResponseEntity.ok(urlService.editUrlDetails(id, request, auth.getName()));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUrl(@PathVariable Long id, Authentication auth){
=======
    ResponseEntity<ResponseObject> createUrl(@Valid @RequestBody CreateUrlRequest urlData, Authentication auth) {
        CreateUrlResponse urlDetails = urlService.createUrl(urlData, auth.getName());

        return ResponseHandler.handleSuccess(200, urlDetails, "Short URL created successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllUserURI(Authentication auth) {
        List<UrlMap> urlList = urlService.getAllUrls(auth.getName());

        return ResponseHandler.handleSuccess(200, urlList, "Created URLs fetched");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUserUri(@PathVariable(value = "id") Long id, Authentication auth) {
        UrlMap urlDetails = urlService.getUrlById(id, auth.getName());

        return ResponseHandler.handleSuccess(200, urlDetails, "URL details fetched.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> editUrlDetails(@PathVariable(value = "id") Long id,
                                                         @RequestBody @Valid EditUrlRequest request, Authentication auth) {
        boolean success = urlService.editUrlDetails(id, request, auth.getName());

        return ResponseHandler.handleSuccess(200, success, "URL details updated");
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUrl(@PathVariable Long id, Authentication auth) {
>>>>>>> Stashed changes
        urlService.deleteUrl(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}