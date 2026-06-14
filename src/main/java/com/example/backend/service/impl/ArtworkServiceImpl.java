package com.example.backend.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;
import com.example.backend.dto.artwork.ArtworkRequest;
import com.example.backend.dto.artwork.ArtworkResponse;
import com.example.backend.exception.NotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.model.Artwork;
import com.example.backend.model.Category;
import com.example.backend.model.User;
import com.example.backend.repository.ArtworkRepository;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ArtworkService;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public ArtworkServiceImpl(
            ArtworkRepository artworkRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            Cloudinary cloudinary
    ) {
        this.artworkRepository = artworkRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }

    private ArtworkResponse toResponse(Artwork a) {
        return new ArtworkResponse(
                a.getId(),
                a.getTitle(),
                a.getDescription(),
                a.getImageUrl(),
                a.getUser().getUsername(),
                a.getCategory().getName()
        );
    }

    private String uploadImageToCloudinary(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    imageFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );

            Object secureUrl = result.get("secure_url");
            return secureUrl != null ? secureUrl.toString() : null;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }


    @Override
    public List<ArtworkResponse> getAll() {
        return artworkRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ArtworkResponse getById(Long id) {
        Artwork a = artworkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artwork not found"));
        return toResponse(a);
    }

    @Override
    public ArtworkResponse create(ArtworkRequest req, Long userId) {
        // deprecated JSON-based create path (kept for compatibility)
        return create(null, req, userId);
    }

    @Override
    public ArtworkResponse create(MultipartFile imageFile, ArtworkRequest req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Category cat = categoryRepository.findByName(req.getCategory())
                .orElseThrow(() -> new NotFoundException("Invalid category"));

        String imageUrl = uploadImageToCloudinary(imageFile);
        if (imageUrl == null || imageUrl.isBlank()) {
            imageUrl = req.getImageUrl();
        }

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("imageFile or imageUrl must be provided");
        }

        Artwork a = Artwork.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .imageUrl(imageUrl)
                .user(user)
                .category(cat)
                .build();

        a = artworkRepository.save(a);
        return toResponse(a);
    }

    @Override
    public ArtworkResponse update(Long id, ArtworkRequest req, Long userId) {
        Artwork a = artworkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artwork not found"));

        if (!a.getUser().getId().equals(userId) && !"admin".equals(a.getUser().getRole())) {
            throw new UnauthorizedException("Forbidden");
        }

        a.setTitle(req.getTitle() != null ? req.getTitle() : a.getTitle());
        a.setDescription(req.getDescription() != null ? req.getDescription() : a.getDescription());
        a.setImageUrl(req.getImageUrl() != null ? req.getImageUrl() : a.getImageUrl());

        if (req.getCategory() != null) {
            Category cat = categoryRepository.findByName(req.getCategory())
                    .orElseThrow(() -> new NotFoundException("Invalid category"));
            a.setCategory(cat);
        }

        a = artworkRepository.save(a);
        return toResponse(a);
    }

    @Override
    public void delete(Long id, Long userId) {
        Artwork a = artworkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artwork not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!a.getUser().getId().equals(userId) && !"admin".equals(user.getRole())) {
            throw new UnauthorizedException("Forbidden");
        }

        artworkRepository.deleteById(id);
    }
}

