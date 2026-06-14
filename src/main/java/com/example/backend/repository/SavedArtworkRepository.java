// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/repository/SavedArtworkRepository.java
package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.SavedArtwork;

public interface SavedArtworkRepository extends JpaRepository<SavedArtwork, Long> {
    List<SavedArtwork> findByUserIdOrderBySavedAtDesc(Long userId);
    boolean existsByUserIdAndArtworkId(Long userId, Long artworkId);
    int deleteByUserIdAndArtworkId(Long userId, Long artworkId);
}
