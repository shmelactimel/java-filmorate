package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;


@Service
public class InMemoryFilmService implements FilmService {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FilmStorage filmStorage;

    @Override
    public Film addLike(Long filmId, Long userId) throws ValidationException {
        if (Optional.ofNullable(userStorage.getUserById(userId)).isEmpty()) {
            throw new NullPointerException("Пользователь с id " + userId + " не найден");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
        return film;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) throws ValidationException {
        if (Optional.ofNullable(userStorage.getUserById(userId)).isEmpty()) {
            throw new NullPointerException("Пользователь с id " + userId + " не найден");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(userId);
        return film;
    }

    @Override
    public List<Film> getTopRatedFilms() {
        List<Film> result = new ArrayList<>(filmStorage.getAllFilms());
        result.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getUsersLike().size() - o1.getUsersLike().size();
            }
        });
        return result;
    }
}
