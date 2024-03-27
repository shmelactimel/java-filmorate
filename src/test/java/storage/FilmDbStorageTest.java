package storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import java.util.List;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

@JdbcTest
@ContextConfiguration(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private Film film;
    private User user;
    private FilmDbStorage filmStorage;
    private UserDbStorage userStorage;

    @BeforeEach
    public void init() {
        film = new Film();
        film.setName("Horror2000");
        film.setDescription("it is terrifying");
        film.setReleaseDate(LocalDate.of(2010, 1, 1));
        film.setDuration(201);
        film.setMpa(new Mpa(1L, "G"));
        user = new User();
        user.setEmail("kkkker@eew.ru");
        user.setLogin("va3fefef3");
        user.setName("Ken Ben");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage = new UserDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, userStorage);
    }

    @Test
    void getFilms_success() throws EntityAlreadyExistException {
        //given
        filmStorage.addFilm(film);
        //when
        List<Film> allFilms = filmStorage.getFilms();
        //then
        assertThat(allFilms)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getFilms_success_noFilms() throws EntityAlreadyExistException {
        //when
        List<Film> allFilms = filmStorage.getFilms();
        //then
        assertThat(allFilms)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void addFilm_success() throws EntityAlreadyExistException {
        //when
        Long id = filmStorage.addFilm(film).getId();
        //then
        assertThat(id)
                .isNotNull()
                .isInstanceOf(Long.class)
                .isNotNegative();
    }

    @Test
    void addFilm_failure() throws EntityAlreadyExistException {
        //given
        Film newFilm = new Film();
        newFilm.setName(film.getName());
        newFilm.setDescription(film.getDescription());
        newFilm.setReleaseDate(film.getReleaseDate());
        newFilm.setDuration(film.getDuration());
        newFilm.setMpa(film.getMpa());
        newFilm.setGenres(film.getGenres());
        //when
        filmStorage.addFilm(film);
        //then
        assertThatThrownBy(() ->
                filmStorage.addFilm(newFilm))
                .isInstanceOf(EntityAlreadyExistException.class)
                .hasMessageContaining("film already exists");
    }

    @Test
    void updateFilm_success() throws EntityAlreadyExistException {
        //given
        Film newFilm = new Film();
        newFilm.setName(film.getName());
        newFilm.setReleaseDate(film.getReleaseDate());
        newFilm.setDuration(film.getDuration());
        newFilm.setMpa(film.getMpa());
        newFilm.setGenres(film.getGenres());
        Long id = filmStorage.addFilm(film).getId();
        //when
        String newDescription = "not much";
        newFilm.setDescription(newDescription);
        Film updatedFilm = filmStorage.updateFilm(id, newFilm);
        //then
        assertThat(updatedFilm)
                .isNotNull()
                .isNotEqualTo(film);
        assertThat(updatedFilm.getId())
                .isNotNull()
                .isEqualTo(film.getId());
    }

    @Test
    void updateFilm_failure_wrongId() throws EntityAlreadyExistException {
        //given
        Film newFilm = new Film();
        newFilm.setName(film.getName());
        newFilm.setReleaseDate(film.getReleaseDate());
        newFilm.setDuration(film.getDuration());
        newFilm.setMpa(film.getMpa());
        //when
        String newDescription = "not much";
        newFilm.setDescription(newDescription);
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.updateFilm(id, newFilm))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("film not found");
    }

    @Test
    void getFilm_success() throws EntityAlreadyExistException {
        //given
        Long id = filmStorage.addFilm(film).getId();
        //when
        Film storedFilm = filmStorage.getFilm(id);
        //then
        assertThat(storedFilm)
                .isNotNull()
                .isEqualTo(film);
    }

    @Test
    void getFilm_failure_wrongId() throws EntityAlreadyExistException {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.getFilm(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("film not found");
    }

    @Test
    void addLike_success() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.addUser(user).getId();
        Long filmId = filmStorage.addFilm(film).getId();
        //when
        filmStorage.addLike(filmId, userId);
        Film likedFilm = filmStorage.getFilm(filmId);
        //then
        assertThat(likedFilm.getLikesCount())
                .isNotNull()
                .isEqualTo(1);
    }

    @Test
    void addLike_failure_wrongUserId() throws EntityAlreadyExistException {
        //given
        Long filmId = filmStorage.addFilm(film).getId();
        //when
        Long wrongUserId = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.addLike(filmId, wrongUserId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void addLike_failure_wrongFilmId() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.addUser(user).getId();
        //when
        Long wrongFilmId = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.addLike(wrongFilmId, userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("film not found");
    }

    @Test
    void removeLike_success() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.addUser(user).getId();
        Long filmId = filmStorage.addFilm(film).getId();
        filmStorage.addLike(filmId, userId);
        Film likedFilm = filmStorage.getFilm(filmId);
        //when
        filmStorage.removeLike(filmId, userId);
        Film unlikedFilm = filmStorage.getFilm(filmId);
        //then
        assertThat(likedFilm.getLikesCount())
                .isNotNull()
                .isEqualTo(1);
        assertThat(unlikedFilm.getLikesCount())
                .isNotNull()
                .isEqualTo(0);
    }

    @Test
    void removeLike_failure_wrongUserId() throws EntityAlreadyExistException {
        //given
        Long filmId = filmStorage.addFilm(film).getId();
        //when
        Long wrongUserId = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.removeLike(filmId, wrongUserId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void removeLike_failure_wrongFilmId() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.addUser(user).getId();
        //when
        Long wrongFilmId = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.removeLike(wrongFilmId, userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("film not found");
    }

    @Test
    void getTopFilms_success() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.addUser(user).getId();
        Long filmId = filmStorage.addFilm(film).getId();
        filmStorage.addLike(filmId, userId);
        //when
        List<Film> topFilms = filmStorage.getTopFilms(1);
        //then
        assertThat(topFilms)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getTopFilms_success_withNoLikes() throws EntityAlreadyExistException {
        //given
        filmStorage.addFilm(film);
        //when
        List<Film> topFilms = filmStorage.getTopFilms(1);
        //then
        assertThat(topFilms)
                .isNotNull()
                .hasSize(0);
    }
}