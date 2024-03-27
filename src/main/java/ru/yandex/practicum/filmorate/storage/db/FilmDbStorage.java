package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Duration;

@Repository("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,@Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getFilms() {
        String request = "select * from movies";
        return jdbcTemplate.query(request, (rs, rowNum) -> mapFilm(rs))
                .stream()
                .map(x -> {
                    x.setLikes(mapFilmLikes(x.getId()));
                    return x; })
                .collect(Collectors.toList());
    }

    @Override
    public Film addFilm(Film film) throws EntityAlreadyExistException {
        log.info("add film , film genres:" + film.getGenres());
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from movies where name = ?", film.getName());
        if (sqlRowSet.next()) {
            throw new EntityAlreadyExistException("film already exists");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("movies")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(FilmService.toMap(film)).longValue());
        if ((film.getGenres() != null) && (film.getGenres().size() > 0)) {
            film.getGenres().stream()
                    .forEach(x -> {
                        jdbcTemplate.update("insert into movie_genre (film_id, genre_id) values (?, ?);",
                                film.getId(), x.getId());
                    });
        }
        return film;
    }

    @Override
    public Film updateFilm(Long filmId, Film film) {
        if (filmId == null) {
            filmId = film.getId();
        }
        if (film.getId() == null) {
            film.setId(filmId);
        }
        log.info("update film id: " + filmId);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from movies where film_id = ?", film.getId());
        if (filmId == null || (!sqlRowSet.next())) {
            throw new EntityNotFoundException("film not found");
        }
        jdbcTemplate.update("update movies set name = ?, description = ?, release_date = ?, duration = ?, " +
                        " movie_mpa_id = ? where film_id = ?", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), filmId);
        if (film.getGenres() != null) {
            jdbcTemplate.update("delete from movie_genre where film_id = ?", film.getId());
            film.getGenres().stream()
                    .forEach(x -> {
                        jdbcTemplate.update("insert into movie_genre (film_id, genre_id) values (?, ?);",
                                film.getId(), x.getId());
                    });
        }
        return film;
    }

    @Override
    public Film getFilm(Long filmId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from movies where film_id = ?", filmId);
        if (sqlRowSet.next()) {
            Film film = mapFilm(sqlRowSet);
            film.setLikes(mapFilmLikes(filmId));
            film = updateFilmGenres(filmId, film);
            return film;
        } else {
            throw new EntityNotFoundException("film not found");
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (!filmExistsInTable(filmId)) {
            throw new EntityNotFoundException("film not found");
        }
        if ((userStorage.getUser(userId) != null)) {
            jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?);", filmId, userId);
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (!filmExistsInTable(filmId)) {
            throw new EntityNotFoundException("film not found");
        }
        if ((userStorage.getUser(userId) != null)) {
            jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?;", filmId, userId);
        }
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String request = "SELECT m.film_id, m.name, m.description, m.release_date, m.duration, m.movie_mpa_id FROM movies AS m \n" +
                "INNER JOIN (SELECT film_id, count(user_id) FROM likes group BY film_id ORDER BY count(user_id) DESC LIMIT ? ) AS TOP \n" +
                "ON m.FILM_ID = TOP.film_id;";
        return jdbcTemplate.query(request, (rs, rowNum) -> mapFilm(rs), count)
                .stream()
                .map(x -> {
                    x.setLikes(mapFilmLikes(x.getId()));
                    return x; })
                .collect(Collectors.toList());
    }

    private Film mapFilm(ResultSet rs) throws SQLException {
        long filmId = rs.getLong("film_id");
        String filmName = rs.getString("name");
        String filmDescription = rs.getString("description");
        LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"));
        Duration duration = Duration.ofMinutes(rs.getLong("duration"));
        Film film = new Film();
        film.setId(filmId);
        film.setName(filmName);
        film.setDescription(filmDescription);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration.toMinutes());
        mapFilmMpa(film, rs.getLong("movie_mpa_id"));
        return film;
    }

    private Film mapFilm(SqlRowSet sqlRowSet) {
        Film film = new Film();
        film.setId(sqlRowSet.getLong("film_id"));
        film.setName(sqlRowSet.getString("name"));
        film.setDescription(sqlRowSet.getString("description"));
        film.setReleaseDate(LocalDate.parse(sqlRowSet.getString("release_date")));
        film.setDuration(sqlRowSet.getLong("duration"));
        mapFilmMpa(film, sqlRowSet.getLong("movie_mpa_id"));
        return film;
    }

    private Set<Long> mapFilmLikes(Long filmId) {
        String request = "SELECT user_id FROM likes WHERE film_id = ?";
        return new HashSet<Long>(jdbcTemplate.queryForList(request, Long.class, filmId));
    }

    private Film mapFilmMpa(Film film, Long mpaId) {
        String request = "SELECT name FROM mpa WHERE mpa_id = ?";
        String mpaName = jdbcTemplate.queryForObject(request, String.class, mpaId);
        film.setMpa(new Mpa(mpaId, mpaName));
        return film;
    }

    private Film updateFilmGenres(Long filmId, Film film) {
        String request = "SELECT g.GENRE_ID, g.NAME FROM movie_genre AS mg " +
                "INNER JOIN GENRE AS g ON mg.GENRE_ID = g.GENRE_ID WHERE film_id = ?";
        List<Genre> genres = jdbcTemplate.query(request, (rs, rowNum) -> mapGenre(rs), filmId)
                .stream()
                .collect(Collectors.toList());
        film.setGenres(genres);
        return film;
    }

    private void updateFilmGenres(Long filmId, Integer genreId) {
        jdbcTemplate.update("INSERT INTO movie_genre (film_id, genre_id) VALUES (?, ?);", filmId, genreId);
    }

    private boolean filmExistsInTable(Long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from movies where film_id = ?", id);
        return sqlRowSet.next();
    }

    private Genre mapGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("name"));
    }
}