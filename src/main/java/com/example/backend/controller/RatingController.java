// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/controller/RatingController.java
package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.rating.RatingRequest;
import com.example.backend.security.SecurityUtils;
import com.example.backend.service.RatingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artworks/{artworkId}/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> submitRating(@PathVariable Long artworkId, @Valid @RequestBody RatingRequest req) {
        Long userId = getUserId();
        ratingService.submitRating(artworkId, req, userId);
        return ResponseEntity.ok(ApiResponse.of(true, "Rating submitted", null));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Object>> getRatingSummary(@PathVariable Long artworkId) {
        Object summary = ratingService.getRatingSummary(artworkId);
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }
}
