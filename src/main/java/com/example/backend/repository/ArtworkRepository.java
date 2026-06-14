// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/repository/ArtworkRepository.java
package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Artwork;
import com.example.backend.model.Category;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    @Query("SELECT a FROM Artwork a LEFT JOIN FETCH a.user LEFT JOIN FETCH a.category")
    List<Artwork> findAll();

    @Query("SELECT a FROM Artwork a LEFT JOIN FETCH a.user LEFT JOIN FETCH a.category WHERE a.id = :id")
    Optional<Artwork> findById(@Param("id") Long id);

    long countByCategory(Category category);
}
