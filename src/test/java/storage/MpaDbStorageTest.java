package storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@ContextConfiguration(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private MpaDbStorage mpaDbStorage;

    @BeforeEach
    public void init() {
        mpaDbStorage = new MpaDbStorage(jdbcTemplate);
    }

    @Test
    void getMpas_success() {
        //when
        List<Mpa> mpas = mpaDbStorage.getMpas();
        //then
        assertThat(mpas)
                .isNotNull()
                .hasSize(5);
    }

    @Test
    void getMpa_success() {
        //given
        String name = "G";
        //when
        Mpa mpa = mpaDbStorage.getMpa(1L);
        //then
        assertThat(mpa)
                .isNotNull();
        assertThat(mpa.getName())
                .isNotNull()
                .isEqualTo(name);
    }

    @Test
    void getMpa_failure_wrongId() {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                mpaDbStorage.getMpa(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("mpa not found");
    }
}