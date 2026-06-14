// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/CommentService.java
package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.comment.CommentRequest;

public interface CommentService {
    List<?> getByArtworkId(Long artworkId);
    List<?> getAllComments();
    void addComment(Long artworkId, CommentRequest req, Long userId);
    void deleteComment(Long commentId, Long userId);
}
