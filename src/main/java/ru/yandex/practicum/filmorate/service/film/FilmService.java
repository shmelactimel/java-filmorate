package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmService {
    public Film addLike(Long filmId, Long userId) throws ValidationException;

    public Film deleteLike(Long filmId, Long userId) throws ValidationException;

    public List<Film> getTopRatedFilms();
}
