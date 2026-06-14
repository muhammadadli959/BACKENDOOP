// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/dto/rating/RatingRequest.java
package com.example.backend.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingRequest {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
