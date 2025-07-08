package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exeption.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class BaseDbChangedStore<T> extends BaseDbStorage<T> {
    public BaseDbChangedStore(JdbcTemplate jdbc, RowMapper<T> mapper) {
        super(jdbc, mapper);
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    protected boolean delete(String query, Long id) {
        int countDelete = jdbc.update(query, id);
        return countDelete > 0;
    }

    protected boolean delete(String query, Object... params) {
        int countDelete = jdbc.update(query, params);
        return countDelete > 0;
    }
}
