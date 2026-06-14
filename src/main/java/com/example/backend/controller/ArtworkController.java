// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/controller/ArtworkController.java
package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.artwork.ArtworkRequest;
import com.example.backend.dto.artwork.ArtworkResponse;
import com.example.backend.security.SecurityUtils;
import com.example.backend.service.ArtworkService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ArtworkResponse>>> getAll() {
        List<ArtworkResponse> res = artworkService.getAll();
        return ResponseEntity.ok(ApiResponse.ok(res));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArtworkResponse>> getById(@PathVariable Long id) {
        ArtworkResponse res = artworkService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(res));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArtworkResponse>> create(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("artistName") String artistName,
            @RequestParam("uploadedBy") String uploadedBy,
            @RequestParam(value = "imageUrl", required = false) String imageUrl
    ) {
        // artistName & uploadedBy are kept for compatibility with frontend request,
        // but current backend uses JWT principal to determine userId.
        Long userId = getUserId();

        ArtworkRequest req = new ArtworkRequest();
        req.setTitle(title);
        req.setDescription(description);
        req.setCategory(category);
        req.setImageUrl(imageUrl);

        // ArtworkServiceImpl will handle saving + imageUrl (from file or imageUrl).
        ArtworkResponse res = artworkService.create(imageFile, req, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(res));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ArtworkResponse>> update(@PathVariable Long id, @Valid @RequestBody ArtworkRequest req) {
        Long userId = getUserId();
        ArtworkResponse res = artworkService.update(id, req, userId);
        return ResponseEntity.ok(ApiResponse.ok(res));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        Long userId = getUserId();
        artworkService.delete(id, userId);
        return ResponseEntity.ok(ApiResponse.of(true, "Artwork deleted", null));
    }
}
