package com.example.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.category.CategoryRequest;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.model.Category;
import com.example.backend.model.User;
import com.example.backend.repository.ArtworkRepository;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            ArtworkRepository artworkRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    @Override
    public List<String> getAllCategoryNames() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String createCategory(CategoryRequest req, Long userId) {
        User user = getAdminUser(userId);
        String name = req.getName().trim();
        if (name.isEmpty()) {
            throw new ConflictException("Category name must not be empty");
        }
        if (categoryRepository.findByName(name).isPresent()) {
            throw new ConflictException("Category already exists");
        }
        Category category = Category.builder()
                .name(name)
                .build();
        return categoryRepository.save(category).getName();
    }

    @Override
    @Transactional
    public void deleteCategory(String name, Long userId) {
        getAdminUser(userId);
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        if (artworkRepository.countByCategory(category) > 0) {
            throw new ConflictException("Cannot delete category that is in use");
        }
        categoryRepository.delete(category);
    }

    private User getAdminUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!"admin".equals(user.getRole())) {
            throw new UnauthorizedException("Forbidden");
        }
        return user;
    }
}
