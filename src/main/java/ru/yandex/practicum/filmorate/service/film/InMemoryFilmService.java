package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Override
    public Film addLike(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        if (Optional.ofNullable(userStorage.getUserById(userId)).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        Film film = filmStorage.getFilmById(filmId);
        if (film.equals(null)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        film.addLike(userId);
        return film;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        if (Optional.ofNullable(userStorage.getUserById(userId)).isEmpty()) {
            throw new NullPointerException("Пользователь с id " + userId + " не найден");
        }
        Film film = filmStorage.getFilmById(filmId);
        if (film.equals(null)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        film.deleteLike(userId);
        return film;
    }

    @Override
    public List<Film> getTopRatedFilms(Integer count) {
        int defaultCount = 10;
        List<Film> result = new ArrayList<>(filmStorage.getAllFilms());
        result.sort((o1, o2) -> o2.getUsersLike().size() - o1.getUsersLike().size());
        if (count > 0) {
            return result.subList(0, count);
        } else {
            if (result.size() < defaultCount) {
                return result;
            } else {
                return result.subList(0, defaultCount);
            }
        }
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) throws FilmNotFoundException {
        filmStorage.updateFilm(film);
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film deleteFilm(long filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    public Film getFilmById(long filmId) throws FilmNotFoundException {
        return filmStorage.getFilmById(filmId);
    }
}
