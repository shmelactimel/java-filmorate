package ru.yandex.practicum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.util.Set;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmTest {
    private Validator validator;
    private Film film;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        String name = "Wars";
        String description = "wars are everywhere";
        LocalDate releaseDate = LocalDate.of(2000,1,1);
        Duration duration = Duration.ofMinutes(100);
        film = new Film();
        film.setDuration(duration.toMinutes());
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
    }

    @Test
    void validateFilmDate_failure_dateInFuture() throws EntityAlreadyExistException {
        //given
        LocalDate releaseDate = LocalDate.of(4000,1,1);
        film.setReleaseDate(releaseDate);
        //when
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateFilmDate_failure_dateIsNull() throws EntityAlreadyExistException {
        //given
        LocalDate releaseDate = null;
        film.setReleaseDate(releaseDate);
        //when
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateFilmDate_failure_dateIsBeforeInitialDate() throws EntityAlreadyExistException {
        //given
        LocalDate releaseDate = LocalDate.of(1000,1,1);
        film.setReleaseDate(releaseDate);
        //when
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateFilmName_failure_emptyName() throws EntityAlreadyExistException {
        //given
        String name = "";
        film.setName(name);
        //when
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateFilmName_failure_nullName() throws EntityAlreadyExistException {
        //given
        String name = null;
        film.setName(name);
        //when
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateFilmDescription_failure_tooBigDescription() throws EntityAlreadyExistException {
        //given
        String description = new String(new char[201]).replace('\0', ' ');
        film.setDescription(description);
        //when
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        //then
        assertFalse(violations.isEmpty());
    }
}