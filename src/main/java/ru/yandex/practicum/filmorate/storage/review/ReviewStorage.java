package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review create(Review review);

    Optional<Review> get(Long id);

    List<Review> getMany(Long id);

    void delete(Long id);

    void setLikeValue(Long id, Long userId, Integer value);

    void deleteLikeValue(Long id, Long userId, Integer value);

    Review update(Review review);

    boolean findExistLikeForReviewUserValue(Long review, Long user, Integer value);
}
