package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.review.ReviewUpdate;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private static final Integer LIKE_VALUE = 1;
    private static final Integer DISLIKE_VALUE = -1;
    private static final int LIMIT = 10;
    private final UserService userService;
    private final FilmService filmService;
    private final ReviewStorage reviewStorage;

    public ReviewDTO getOneReview(Long id) {
        return ReviewMapper.mapToReviewDTO(
                reviewStorage.get(id).orElseThrow(() -> new NotFoundResource("Отзыв не найден")));
    }

    public List<ReviewDTO> getReviews(Long filmId, Integer count) {
        return reviewStorage.getMany(filmId).stream()
                .map(ReviewMapper::mapToReviewDTO)
                .limit(count == null ? LIMIT : count)
                .toList();
    }

    public ReviewDTO create(ReviewCreate newReview) {
        // проверки
        userService.getUser(newReview.getUserId());
        filmService.getFilm(newReview.getFilmId());
        return ReviewMapper.mapToReviewDTO(reviewStorage.create(ReviewMapper.mapToReview(newReview)));
    }

    public void delete(Long id) {
        reviewStorage.get(id).orElseThrow(() -> new NotFoundResource("Отзыв не найден"));
        reviewStorage.delete(id);
    }

    private void checkAndSetLikeValue(Long id, Long userId, Integer value) {
        reviewStorage.get(id).orElseThrow(() -> new NotFoundResource("Отзыв не найден"));
        // проверка
        userService.getUser(userId);
        reviewStorage.setLikeValue(id, userId, value);
    }

    public void setLikeReview(Long id, Long userId) {
        checkAndSetLikeValue(id, userId, LIKE_VALUE);
    }

    public void setDisLikeReview(Long id, Long userId) {
        checkAndSetLikeValue(id, userId, DISLIKE_VALUE);
    }

    private void deleteLikeValue(Long id, Long userId, Integer value) {
        if (!reviewStorage.findExistLikeForReviewUserValue(id, userId, value))
            throw new NotFoundResource("Нечего удалять");
        reviewStorage.deleteLikeValue(id, userId, value);
    }

    public void deleteLike(Long id, Long userId) {
        deleteLikeValue(id, userId, LIKE_VALUE);
    }

    public void deleteDisLike(Long id, Long userId) {
        deleteLikeValue(id, userId, DISLIKE_VALUE);
    }

    public ReviewDTO update(ReviewUpdate reviewUpdate) {
        Review reviewOld = reviewStorage
                .get(reviewUpdate.getReviewId()).orElseThrow(() -> new NotFoundResource("Отзыв не найден"));
        Review review = ReviewMapper.updateReviewFields(reviewOld, reviewUpdate);
        // проверки
        filmService.getFilm(review.getFilm());
        userService.getUser(review.getUser());
        return ReviewMapper.mapToReviewDTO(reviewStorage.update(review));
    }

}
