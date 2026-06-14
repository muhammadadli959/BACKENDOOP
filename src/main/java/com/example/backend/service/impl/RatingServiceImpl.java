// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/impl/RatingServiceImpl.java
package com.example.backend.service.impl;

import org.springframework.stereotype.Service;

import com.example.backend.dto.rating.RatingRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Artwork;
import com.example.backend.model.Rating;
import com.example.backend.model.RatingId;
import com.example.backend.model.User;
import com.example.backend.repository.ArtworkRepository;
import com.example.backend.repository.RatingRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.RatingService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, ArtworkRepository artworkRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void submitRating(Long artworkId, RatingRequest req, Long userId) {
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new NotFoundException("Artwork not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        RatingId ratingId = new RatingId(artworkId, userId);
        Optional<Rating> existing = ratingRepository.findByIdArtworkIdAndIdUserId(artworkId, userId);
        if (existing.isPresent()) {
            Rating rating = existing.get();
            rating.setRating(req.getRating());
            ratingRepository.save(rating);
        } else {
            Rating rating = Rating.builder()
                    .id(ratingId)
                    .artwork(artwork)
                    .user(user)
                    .rating(req.getRating())
                    .build();
            ratingRepository.save(rating);
        }
    }

    @Override
    public Object getRatingSummary(Long artworkId) {
        artworkRepository.findById(artworkId).orElseThrow(() -> new NotFoundException("Artwork not found"));
        List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(r -> r.getId().getArtworkId().equals(artworkId)).toList();
        long count = ratings.size();
        double average = ratings.isEmpty() ? 0.0 : ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
        
        return new Object() {
            public long getCount() { return count; }
            public double getAverage() { return Math.round(average * 10.0) / 10.0; }
        };
    }
}
