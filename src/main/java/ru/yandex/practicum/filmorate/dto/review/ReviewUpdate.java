package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewUpdate {
    private Long reviewId;
    private String content;
    @JsonProperty("isPositive")
    private Boolean isPositive;
    private Long userId;
    private Long filmId;

    public boolean hasContent() {
        return !(content == null || content.isBlank());
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasFilmId() {
        return filmId != null;
    }

    public boolean hasPositive() {
        return isPositive != null;
    }

}
