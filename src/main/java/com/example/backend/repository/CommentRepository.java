// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/repository/CommentRepository.java
package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArtworkIdOrderByIdAsc(Long artworkId);
}
