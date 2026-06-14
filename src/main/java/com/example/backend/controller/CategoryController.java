package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.category.CategoryRequest;
import com.example.backend.security.SecurityUtils;
import com.example.backend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getAllCategoryNames() {
        List<String> names = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(ApiResponse.ok(names));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> createCategory(@Valid @RequestBody CategoryRequest request) {
        String created = categoryService.createCategory(request, getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(true, "Category created", created));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@RequestParam String name) {
        categoryService.deleteCategory(name, getUserId());
        return ResponseEntity.ok(ApiResponse.of(true, "Category deleted", null));
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }
}

