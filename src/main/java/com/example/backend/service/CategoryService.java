package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.category.CategoryRequest;

public interface CategoryService {
    List<String> getAllCategoryNames();
    String createCategory(CategoryRequest req, Long userId);
    void deleteCategory(String name, Long userId);
}
