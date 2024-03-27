package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository("mpaDbStorage")
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpas() {
        String request = "select * from mpa";
        return jdbcTemplate.query(request, (rs, rowNum) -> mapMpa(rs))
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Mpa getMpa(Long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from mpa where mpa_id = ?", id);
        if (sqlRowSet.next()) {
            return mapMpa(sqlRowSet);
        } else {
            throw new EntityNotFoundException("mpa not found");
        }
    }

    private Mpa mapMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("mpa_id"), rs.getString("name"));
    }

    private Mpa mapMpa(SqlRowSet sqlRowSet) {
        return new Mpa(sqlRowSet.getLong("mpa_id"), sqlRowSet.getString("name"));
    }
}