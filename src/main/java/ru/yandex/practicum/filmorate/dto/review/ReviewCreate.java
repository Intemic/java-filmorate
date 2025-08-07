package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

@Builder
@Data
public class ReviewCreate {
    @NotBlank
    private String content;
    @JsonProperty("isPositive")
    @NonNull
    private Boolean isPositive;
    @NonNull
    private Long userId;
    @NonNull
    private Long filmId;
}
