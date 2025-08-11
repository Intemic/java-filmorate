package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewDTO {
    private Long reviewId;
    private String content;
    @JsonProperty("isPositive")
    private boolean isPositive;
    private Long userId;
    private Long filmId;
    private int useful;
}
