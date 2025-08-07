package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDbStorage extends BaseDbStorage<Review> implements ReviewStorage {
    private static final String INSERT_QUERY = "INSERT INTO reviews (film_id, user_id, content, positiv) " +
            "VALUES(?, ?, ?, ?)";
    private static final String FIND_ONE = "SELECT r.id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.positiv, " +
            "(SELECT sum(like_value) " +
            "FROM reviews_likes " +
            "WHERE review_id = r.id ) AS useful " +
            "FROM reviews AS r " +
            "WHERE r.id = ?";
    private static final String FIND_REVIEW_BY_FILM = "SELECT r.id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.positiv, " +
            "IFNULL ((SELECT sum(like_value) " +
            "FROM reviews_likes " +
            "WHERE review_id = r.id ), 0) AS useful " +
            "FROM reviews AS r " +
            "WHERE r.film_id = ? " +
            "ORDER BY useful DESC";
    private static final String FIND_ALL_REVIEW = "SELECT r.id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.positiv, " +
            "IFNULL ((SELECT sum(like_value) " +
            "FROM reviews_likes " +
            "WHERE review_id = r.id ), 0) AS useful " +
            "FROM reviews AS r " +
            "ORDER BY useful DESC";
    private static final String DELETE = "DELETE FROM reviews WHERE id = ?";
    private static final String SET_LIKE_VALUE = "MERGE INTO REVIEWS_LIKES AS T USING " +
            "    (VALUES (?, ?, ?)) AS S(REVIEW_ID, USER_ID, LIKE_VALUE) " +
            "    ON  T.REVIEW_ID = S.REVIEW_ID " +
            "    AND T.USER_ID = S.USER_ID " +
            "    WHEN MATCHED THEN " +
            "        UPDATE SET T.LIKE_VALUE = S.LIKE_VALUE " +
            "    WHEN NOT MATCHED THEN " +
            "        INSERT (REVIEW_ID, USER_ID, LIKE_VALUE) VALUES (S.REVIEW_ID, S.USER_ID, S.LIKE_VALUE)";
    private static final String DELETE_LIKE_VALUE = "DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ? " +
            "AND like_value = ?";
    private static final String UPDATE_QUERY = "UPDATE reviews SET film_id = ?, user_id = ?, content = ?, " +
            "positiv = ? WHERE id = ?";
    private static final String FIND_LIKE_BY_ID_USER_VALUE =
            "SELECT COUNT(*) FROM reviews_likes WHERE review_id = ? AND user_id = ? AND like_value = ?";
    private static final String UPDATE_LIKE_VALUE =
            "UPDATE reviews_likes SET like_value = ? WHERE review_id = ? AND user_id = ?";
    public static final String INSERT_EVENT_QUERY = "INSERT INTO feed" +
            "(timestamp, user_id, event_type, operation, entity_id)" +
            "VALUES (?, ?, ?, ?, ?)";

    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Review create(Review review) {
        review.setId(insert(INSERT_QUERY,
                review.getFilm(),
                review.getUser(),
                review.getContent(),
                review.isPositive()));
//        insert(INSERT_EVENT_QUERY,
//                Timestamp.from(Instant.now()),
//                review.getUser(),
//                "REVIEW",
//                "ADD",
//                review.getId());
        return review;
    }

    @Override
    public Optional<Review> get(Long id) {
        return getSingle(FIND_ONE, id);
    }

    @Override
    public List<Review> getMany(Long id) {
        if (id != null)
            return getMany(FIND_REVIEW_BY_FILM, id);
        else
            return getMany(FIND_ALL_REVIEW);
    }

    @Override
    public void delete(Long id) {
        Review review = getSingle(FIND_ONE, id).get();
        delete(DELETE, id);
//        insert(INSERT_EVENT_QUERY,
//                Timestamp.from(Instant.now()),
//                review.getUser(),
//                "REVIEW",
//                "REMOVE",
//                id);
    }

    @Override
    public void setLikeValue(Long id, Long userId, Integer value) {
        update(SET_LIKE_VALUE, id, userId, value);
    }

    @Override
    public void deleteLikeValue(Long id, Long userId, Integer value) {
        delete(DELETE_LIKE_VALUE, id, userId, value);
    }

    @Override
    public Review update(Review review) {
        update(UPDATE_QUERY,
                review.getFilm(),
                review.getUser(),
                review.getContent(),
                review.isPositive(),
                review.getId());
//        insert(INSERT_EVENT_QUERY,
//                Timestamp.from(Instant.now()),
//                review.getUser(),
//                "REVIEW",
//                "UPDATE",
//                review.getId());
        return review;
    }

    @Override
    public boolean findExistLikeForReviewUserValue(Long review, Long user, Integer value) {
        return jdbc.queryForObject(FIND_LIKE_BY_ID_USER_VALUE, Integer.class,
                new Object[]{review, user, value}) != 0;
    }

}
