package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public Film addLike(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException;

    public Film deleteLike(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException;

    public List<Film> getTopRatedFilms(Integer count);

    public Film addFilm(Film film);

    public Film updateFilm(Film film) throws FilmNotFoundException;

    public List<Film> getAllFilms();

    public Film deleteFilm(long filmId);

    public Film getFilmById(long filmId) throws FilmNotFoundException;
}
