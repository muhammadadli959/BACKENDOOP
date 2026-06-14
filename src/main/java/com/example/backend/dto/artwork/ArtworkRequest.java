// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/dto/artwork/ArtworkRequest.java
package com.example.backend.dto.artwork;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArtworkRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String category;

    private String imageUrl;

}
