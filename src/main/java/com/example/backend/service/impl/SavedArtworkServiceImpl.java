// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/impl/SavedArtworkServiceImpl.java
package com.example.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.artwork.ArtworkResponse;
import com.example.backend.dto.saved.SavedResponse;
import com.example.backend.exception.NotFoundException;
import com.example.backend.exception.ConflictException;
import com.example.backend.model.Artwork;
import com.example.backend.model.SavedArtwork;
import com.example.backend.model.User;
import com.example.backend.repository.ArtworkRepository;
import com.example.backend.repository.SavedArtworkRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.SavedArtworkService;

@Service
public class SavedArtworkServiceImpl implements SavedArtworkService {

    private final SavedArtworkRepository savedArtworkRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public SavedArtworkServiceImpl(SavedArtworkRepository savedArtworkRepository, ArtworkRepository artworkRepository, UserRepository userRepository) {
        this.savedArtworkRepository = savedArtworkRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Override
    // @Transactional(readOnly = true) // Commenting out the existing annotation
    @Transactional(readOnly = true) // Adding the annotation again for clarity
    public List<ArtworkResponse> getSavedArtworks(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<SavedArtwork> saved = savedArtworkRepository.findByUserIdOrderBySavedAtDesc(userId);
        return saved.stream().map(sa -> {
            Artwork a = sa.getArtwork();
            return new ArtworkResponse(a.getId(), a.getTitle(), a.getDescription(), a.getImageUrl(), a.getUser().getUsername(), a.getCategory().getName());
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isSaved(Long userId, Long artworkId) {
        return savedArtworkRepository.existsByUserIdAndArtworkId(userId, artworkId);
    }

    @Override
    public SavedResponse saveArtwork(Long userId, Long artworkId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new NotFoundException("Artwork not found"));
        
        if (savedArtworkRepository.existsByUserIdAndArtworkId(userId, artworkId)) {
            throw new ConflictException("Already saved");
        }
        
        SavedArtwork saved = SavedArtwork.builder()
                .user(user)
                .artwork(artwork)
                .build();
        saved = savedArtworkRepository.save(saved);
        return new SavedResponse(saved.getId(), saved.getArtwork().getId(), saved.getSavedAt());
    }

    @Override
    @Transactional
    public void unsaveArtwork(Long userId, Long artworkId) {
        int deleted = savedArtworkRepository.deleteByUserIdAndArtworkId(userId, artworkId);
        if (deleted == 0) {
            throw new NotFoundException("Save not found");
        }
    }
}
