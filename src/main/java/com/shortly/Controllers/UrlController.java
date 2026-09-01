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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;
    public UrlController(UrlService service){
        this.urlService = service;
    }

    @GetMapping("/get/{shortCode}")
    ResponseEntity<?> getUrl(@PathVariable String shortCode) throws URISyntaxException{
        String longUrl = urlService.getUrl(shortCode);
        System.out.print("Get url hit");

        URI targetUri = new URI(longUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(targetUri)
                .build();
    }

    @PostMapping("/create")
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
    public ResponseEntity<UrlMap> getUserUri(@PathVariable(value = "id") Integer id, Authentication auth){
        return ResponseEntity.ok(urlService.getUrlById(id, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UrlMap> editUrlDetails(@PathVariable(value = "id") Integer id,
                                                 @RequestBody EditUrlRequest request, Authentication auth){
        return ResponseEntity.ok(urlService.editUrlDetails(id, request, auth.getName()));
    }

//    @PutMapping("/disable")
//    ResponseEntity<String> disableUrl(@Valid @RequestBody EditUrlRequest input, Authentication auth){
//        return ResponseEntity.ok(urlService.disableUrl(input.shortCode(), auth.getName()));
//    }

//    @PutMapping("/enable")
//    ResponseEntity<String> enableUrl(@Valid @RequestBody EditUrlRequest input, Authentication auth){
//        return ResponseEntity.ok(urlService.enableUrl(input.shortCode(), auth.getName()));
//    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUrl(@PathVariable Long id, Authentication auth){
        urlService.deleteUrl(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
