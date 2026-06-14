// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/controller/CommentController.java
package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.comment.CommentRequest;
import com.example.backend.security.SecurityUtils;
import com.example.backend.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }

    @PostMapping("/artworks/{artworkId}/comments")
    public ResponseEntity<ApiResponse<Object>> addComment(@PathVariable Long artworkId, @Valid @RequestBody CommentRequest req) {
        Long userId = getUserId();
        commentService.addComment(artworkId, req, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(true, "Comment added", null));
    }

    @GetMapping("/artworks/{artworkId}/comments")
    public ResponseEntity<ApiResponse<List<?>>> getComments(@PathVariable Long artworkId) {
        List<?> comments = commentService.getByArtworkId(artworkId);
        return ResponseEntity.ok(ApiResponse.ok(comments));
    }

    @GetMapping("/admin/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<?>>> getAllComments() {
        List<?> comments = commentService.getAllComments();
        return ResponseEntity.ok(ApiResponse.ok(comments));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long commentId) {
        Long userId = getUserId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.of(true, "Comment deleted", null));
    }
}
