// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/controller/SavedArtworkController.java
package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.artwork.ArtworkResponse;
import com.example.backend.dto.saved.SavedResponse;
import com.example.backend.security.SecurityUtils;
import com.example.backend.service.SavedArtworkService;

@RestController
@RequestMapping("/api/artworks")
public class SavedArtworkController {

    private final SavedArtworkService savedArtworkService;

    public SavedArtworkController(SavedArtworkService savedArtworkService) {
        this.savedArtworkService = savedArtworkService;
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }

    @GetMapping("/user/saved")
    public ResponseEntity<ApiResponse<List<ArtworkResponse>>> getSavedArtworks() {
        Long userId = getUserId();
        List<ArtworkResponse> saved = savedArtworkService.getSavedArtworks(userId);
        return ResponseEntity.ok(ApiResponse.ok(saved));
    }

    @GetMapping("/{artworkId}/is-saved")
    public ResponseEntity<ApiResponse<Object>> checkSaved(@PathVariable Long artworkId) {
        Long userId = getUserId();
        boolean isSaved = savedArtworkService.isSaved(userId, artworkId);
        return ResponseEntity.ok(ApiResponse.ok(new Object() {
            public boolean getIsSaved() { return isSaved; }
        }));
    }

    @PostMapping("/{artworkId}/save")
    public ResponseEntity<ApiResponse<SavedResponse>> saveArtwork(@PathVariable Long artworkId) {
        Long userId = getUserId();
        SavedResponse res = savedArtworkService.saveArtwork(userId, artworkId);
        return ResponseEntity.ok(ApiResponse.of(true, "Artwork saved", res));
    }

    @DeleteMapping("/{artworkId}/unsave")
    public ResponseEntity<ApiResponse<Object>> unsaveArtwork(@PathVariable Long artworkId) {
        Long userId = getUserId();
        savedArtworkService.unsaveArtwork(userId, artworkId);
        return ResponseEntity.ok(ApiResponse.of(true, "Artwork removed from saved", null));
    }
}
