package ru.yandex.practicum.filmorate.storage.film;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

@Component
public interface FilmStorage {
    public Film addFilm(Film film);

    public Film deleteFilm(long filmId);

    public Film updateFilm(Film film) throws ValidationException;

    public List<Film> getAllFilms();

    public Film getFilmById(long filmId);
}
