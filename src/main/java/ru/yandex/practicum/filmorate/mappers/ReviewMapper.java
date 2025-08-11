package ru.yandex.practicum.filmorate.mappers;


import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.review.ReviewUpdate;
import ru.yandex.practicum.filmorate.model.Review;

@UtilityClass
public class ReviewMapper {
    public static Review mapToReview(ReviewCreate newReview) {
        return Review.builder()
                .content(newReview.getContent())
                .isPositive(newReview.getIsPositive())
                .user(newReview.getUserId())
                .film(newReview.getFilmId()).build();
    }

    public static ReviewDTO mapToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPositive(review.isPositive())
                .userId(review.getUser())
                .filmId(review.getFilm())
                .useful(review.getUseful())
                .build();
    }

    public static Review updateReviewFields(Review review, ReviewUpdate reviewUpdate) {
        if (reviewUpdate.hasContent())
            review.setContent(reviewUpdate.getContent());

        if (reviewUpdate.hasPositive())
            review.setPositive(reviewUpdate.getIsPositive());

        return review;
    }

}
