// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/RatingService.java
package com.example.backend.service;

import com.example.backend.dto.rating.RatingRequest;

public interface RatingService {
    void submitRating(Long artworkId, RatingRequest req, Long userId);
    Object getRatingSummary(Long artworkId);
}
