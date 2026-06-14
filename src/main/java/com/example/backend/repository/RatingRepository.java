// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/repository/RatingRepository.java
package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Rating;
import com.example.backend.model.RatingId;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    Optional<Rating> findByIdArtworkIdAndIdUserId(Long artworkId, Long userId);
}
