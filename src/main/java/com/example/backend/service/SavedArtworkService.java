// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/SavedArtworkService.java
package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.artwork.ArtworkResponse;
import com.example.backend.dto.saved.SavedResponse;

public interface SavedArtworkService {
    List<ArtworkResponse> getSavedArtworks(Long userId);
    boolean isSaved(Long userId, Long artworkId);
    SavedResponse saveArtwork(Long userId, Long artworkId);
    void unsaveArtwork(Long userId, Long artworkId);
}
