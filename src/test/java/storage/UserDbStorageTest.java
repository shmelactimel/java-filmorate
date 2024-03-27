package storage;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.List;
import java.util.Set;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.FilmorateApplication;

@JdbcTest
@ContextConfiguration(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private User user;
    private UserDbStorage userStorage;

    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail("kkkker@eew.ru");
        user.setLogin("va3fefef3");
        user.setName("Ken Ben");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    void getAllUsers_success() throws EntityAlreadyExistException {
        //given
        userStorage.addUser(user);
        //when
        List<User> allUsers = userStorage.getUsers();
        //then
        assertThat(allUsers)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getAllUsers_success_noUsers() throws EntityAlreadyExistException {
        //when
        List<User> allUsers = userStorage.getUsers();
        //then
        assertThat(allUsers)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void getUser_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        //when
        User storedUser = userStorage.getUser(id);
        //then
        assertThat(storedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    void getUser_failure_wrongId() throws EntityAlreadyExistException {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.getUser(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void addUser_success() throws EntityAlreadyExistException {
        //when
        Long id = userStorage.addUser(user).getId();
        //then
        assertThat(id)
                .isNotNull()
                .isInstanceOf(Long.class)
                .isNotNegative();
    }

    @Test
    void addUser_failure() throws EntityAlreadyExistException {
        //given
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setLogin(user.getLogin());
        newUser.setName(user.getName());
        newUser.setBirthday(user.getBirthday());
        //when
        userStorage.addUser(user);
        //then
        assertThatThrownBy(() ->
                userStorage.addUser(newUser))
                .isInstanceOf(EntityAlreadyExistException.class)
                .hasMessageContaining("user already exists");
    }

    @Test
    void updateUser_success() throws EntityNotFoundException, EntityAlreadyExistException {
        //given
        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setName(user.getName());
        newUser.setBirthday(user.getBirthday());
        //when
        String newEmail = "newemail@new.com";
        newUser.setEmail(newEmail);
        Long id = userStorage.addUser(user).getId();
        User updatedUser = userStorage.updateUser(id, newUser);
        //then
        assertThat(updatedUser)
                .isNotNull()
                .isNotEqualTo(user);
        assertThat(updatedUser.getId())
                .isNotNull()
                .isEqualTo(user.getId());
    }

    @Test
    void updateUser_failure_wrongId() throws EntityNotFoundException {
        //given
        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setName(user.getName());
        newUser.setBirthday(user.getBirthday());
        //when
        String newEmail = "newemail@new.com";
        newUser.setEmail(newEmail);
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.updateUser(id, newUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void addFriend_success() throws EntityAlreadyExistException {
        //when
        Long id = userStorage.addUser(user).getId();
        //then
        assertThat(id)
                .isNotNull()
                .isInstanceOf(Long.class)
                .isNotNegative();
    }

    @Test
    void addFriend_failure_withWrongId() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        //when
        Long newId = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.addFriend(id, newId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void removeFriend_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        String newLogin = "newlogin";
        user.setLogin(newLogin);
        Long newId = userStorage.addUser(user).getId();
        //when
        userStorage.addFriend(id, newId);
        Set<User> friends = userStorage.getFriends(id);
        userStorage.removeFriend(id, newId);
        Set<User> friendsRemoved = userStorage.getFriends(id);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(1);
        assertThat(friendsRemoved)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void removeFriend_failure_withWrongId() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        //when
        Long wrongId = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.removeFriend(id, wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void removeFriend_failure_notFriends() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        //when
        String newLogin = "newlogin";
        user.setLogin(newLogin);
        Long newId = userStorage.addUser(user).getId();
        //then
        assertThatThrownBy(() ->
                userStorage.removeFriend(id, newId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("friends are not found");
    }

    @Test
    void getCommonFriends_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        String secondLogin = "secondlogin";
        user.setLogin(secondLogin);
        Long secondId = userStorage.addUser(user).getId();
        String thirdLogin = "thirdlogin";
        user.setLogin(thirdLogin);
        Long thirdId = userStorage.addUser(user).getId();
        //when
        userStorage.addFriend(id, secondId);
        userStorage.addFriend(id, thirdId);
        userStorage.addFriend(secondId, thirdId);
        List<User> friends = userStorage.getCommonFriends(id, secondId);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getCommonFriends_success_noCommonFriends() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        String secondLogin = "secondlogin";
        user.setLogin(secondLogin);
        Long secondId = userStorage.addUser(user).getId();
        //when
        List<User> friends = userStorage.getCommonFriends(id, secondId);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void getFriends_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.addUser(user).getId();
        String secondLogin = "secondlogin";
        user.setLogin(secondLogin);
        Long secondId = userStorage.addUser(user).getId();
        //when
        userStorage.addFriend(id, secondId);
        Set<User> friends = userStorage.getFriends(id);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getFriends_failure_wrongId() throws EntityAlreadyExistException {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.getFriends(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void approveFriend_failure_wrongId() throws EntityAlreadyExistException {
        //when
        Long id = userStorage.addUser(user).getId();
        Long wrongId = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.approveFriend(id, wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void approveFriend_failure_notFriend() throws EntityAlreadyExistException {
        //when
        Long id = userStorage.addUser(user).getId();
        String secondLogin = "secondlogin";
        user.setLogin(secondLogin);
        Long secondId = userStorage.addUser(user).getId();
        //then
        assertThatThrownBy(() ->
                userStorage.approveFriend(id, secondId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("friendship not found");
    }
}