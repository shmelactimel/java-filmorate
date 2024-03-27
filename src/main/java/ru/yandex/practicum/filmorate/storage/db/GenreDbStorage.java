package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.List;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Repository("genreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String request = "select * from genre";
        return jdbcTemplate.query(request, (rs, rowNum) -> mapGenre(rs))
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenre(Long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from genre where genre_id = ?", id);
        if (sqlRowSet.next()) {
            return mapGenre(sqlRowSet);
        } else {
            throw new EntityNotFoundException("genre not found");
        }
    }

    private Genre mapGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("name"));
    }

    private Genre mapGenre(SqlRowSet sqlRowSet) {
        return new Genre(sqlRowSet.getLong("genre_id"), sqlRowSet.getString("name"));
    }
}