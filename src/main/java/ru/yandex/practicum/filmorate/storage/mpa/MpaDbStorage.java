package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Mpa> get(Long id) {
        return getSingle(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Mpa> getAll() {
        return getMany(FIND_ALL_QUERY);
    }
}
