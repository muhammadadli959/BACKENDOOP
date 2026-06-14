// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/dto/artwork/ArtworkResponse.java
package com.example.backend.dto.artwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String ownerUsername;
    private String category;
}
