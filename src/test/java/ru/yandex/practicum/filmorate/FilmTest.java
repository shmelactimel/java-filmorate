package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmTest {
    Validator validator;
    Film film;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testFilmCreationWithoutName() {
        film = new Film();
        film.setDescription("Some Description");
        film.setReleaseDate(LocalDate.of(1986, Month.JUNE, 8));
        film.setDuration(140);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 2);
    }

    @Test
    public void testFilmCreationWithNameBlank() {
        film = new Film();
        film.setName("");
        film.setDescription("Some Description");
        film.setReleaseDate(LocalDate.of(1986, Month.JUNE, 8));
        film.setDuration(140);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testFilmCreationWithBigDescription() {
        film = new Film();
        film.setName("testFilm Name");
        film.setDescription("Test Big Description: Тут 201 символ...........................................................................................................................................................................................");
        film.setReleaseDate(LocalDate.of(1986, Month.JUNE, 8));
        film.setDuration(140);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testFilmCreationWithoutReleaseDate() {
        film = new Film();
        film.setName("testFilm Name");
        film.setDescription("Some Description");
        film.setDuration(140);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testFilmCreationWithIncorrectReleaseDate() {
        film = new Film();
        film.setName("testFilm Name");
        film.setDescription("Some Description");
        film.setReleaseDate(LocalDate.of(1545, Month.JUNE, 8));
        film.setDuration(140);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testFilmCreationWithNegativeDuration() {
        film = new Film();
        film.setName("testFilm Name");
        film.setDescription("Some Description");
        film.setReleaseDate(LocalDate.of(1964, Month.JUNE, 8));
        film.setDuration(-140);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }
}
