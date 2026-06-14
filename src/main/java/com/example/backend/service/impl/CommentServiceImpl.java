// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/impl/CommentServiceImpl.java
package com.example.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.comment.CommentRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.model.Artwork;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.repository.ArtworkRepository;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CommentService;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ArtworkRepository artworkRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<?> getByArtworkId(Long artworkId) {
        artworkRepository.findById(artworkId).orElseThrow(() -> new NotFoundException("Artwork not found"));
        return commentRepository.findByArtworkIdOrderByIdAsc(artworkId).stream().map(c -> new Object() {
            public Long id = c.getId();
            public String content = c.getContent();
            public String username = c.getUser().getUsername();
            public Long artworkId = c.getArtwork().getId();
        }).collect(Collectors.toList());
    }

    @Override
    public List<?> getAllComments() {
        return commentRepository.findAll().stream().map(c -> new Object() {
            public Long id = c.getId();
            public String content = c.getContent();
            public String username = c.getUser().getUsername();
            public Long artworkId = c.getArtwork().getId();
            public String artworkTitle = c.getArtwork().getTitle();
            public String createdAt = c.getCreatedAt() != null ? c.getCreatedAt().toString() : null;
        }).collect(Collectors.toList());
    }

    @Override
    public void addComment(Long artworkId, CommentRequest req, Long userId) {
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new NotFoundException("Artwork not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = Comment.builder()
                .artwork(artwork)
                .user(user)
                .content(req.getContent())
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (!comment.getUser().getId().equals(userId) && !"admin".equals(user.getRole())) {
            throw new UnauthorizedException("Forbidden");
        }
        commentRepository.deleteById(commentId);
    }
}
