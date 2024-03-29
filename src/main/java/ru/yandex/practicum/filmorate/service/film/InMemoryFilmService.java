package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Override
    public Film addLike(Long filmId, Long userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
        return film;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(userId);
        return film;
    }

    @Override
    public List<Film> getTopRatedFilms(Integer count) {
        int defaultCount = 10;
        List<Film> films = filmStorage.getAllFilms();

        List<Film> result = films.stream()
                .sorted((o1, o2) -> o2.getUsersLike().size() - o1.getUsersLike().size())
                .limit(count != null && count > 0 ? count : defaultCount)
                .collect(Collectors.toList());

        return result;
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film deleteFilm(long filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }
}
