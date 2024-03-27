package storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@ContextConfiguration(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreDbStorage genreDbStorage;

    @BeforeEach
    public void init() {
        genreDbStorage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    void getGenres_success() {
        //when
        List<Genre> genres = genreDbStorage.getGenres();
        //then
        assertThat(genres)
                .isNotNull()
                .hasSize(6);
    }

    @Test
    void getGenre_success() {
        //given
        String name = "Комедия";
        //when
        Genre genre = genreDbStorage.getGenre(1L);
        //then
        assertThat(genre)
                .isNotNull();
        assertThat(genre.getName())
                .isNotNull()
                .isEqualTo(name);
    }

    @Test
    void getGenre_failure_wrongId() {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                genreDbStorage.getGenre(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("genre not found");
    }
}