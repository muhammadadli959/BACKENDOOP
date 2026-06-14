// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/ArtworkService.java
package com.example.backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.artwork.ArtworkRequest;
import com.example.backend.dto.artwork.ArtworkResponse;


public interface ArtworkService {
    List<ArtworkResponse> getAll();
    ArtworkResponse getById(Long id);
    ArtworkResponse create(ArtworkRequest req, Long userId);
    ArtworkResponse create(MultipartFile imageFile, ArtworkRequest req, Long userId);

    ArtworkResponse update(Long id, ArtworkRequest req, Long userId);
    void delete(Long id, Long userId);
}
