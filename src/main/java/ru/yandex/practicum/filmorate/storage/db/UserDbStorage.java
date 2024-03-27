package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.*;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Repository("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String request = "select * from users";
        return jdbcTemplate.query(request, (rs, rowNum) -> mapUser(rs))
                .stream()
                .map(x -> {
                    x.setFriends(mapUserFriends(x.getId()));
                    return x; })
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(Long userId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", userId);
        if (sqlRowSet.next()) {
            User user = mapUser(sqlRowSet);
            user.setFriends(mapUserFriends(userId));
            return user;
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }

    @Override
    public User addUser(User user) throws EntityAlreadyExistException {
        userNameAutoCompletion(user);
        SqlRowSet sqlUserSet = jdbcTemplate.queryForRowSet("select * from users where login = ?", user.getLogin());
        if (sqlUserSet.next()) {
            throw new EntityAlreadyExistException("user already exists");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(UserService.toMap(user)).longValue());
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (userId == null) {
            userId = user.getId();
        }
        if (user.getId() == null) {
            user.setId(userId);
        }
        log.info("update user id: " + userId);
        if (userId == null || !userExistsInTable(userId)) {
            throw new EntityNotFoundException("user not found");
        }
        userNameAutoCompletion(user);
        jdbcTemplate.update("update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if ((userId == null) || (friendId == null) || (!userExistsInTable(userId)) || (!userExistsInTable(friendId))) {
            throw new EntityNotFoundException("user not found");
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from friends where user_id = ? and friends_id = ?",
                friendId, userId);
        if (sqlRowSet.next()) {
            return;
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("approved_friends")
                .usingGeneratedKeyColumns("approve_id");
        Map<String, Boolean> requestMap = new HashMap<>();
        requestMap.put("approved", false);
        Long approveId = simpleJdbcInsert.executeAndReturnKey(requestMap).longValue();
        jdbcTemplate.update("insert into friends (user_id, friends_id, approve_id) values (?, ?, ?);", userId, friendId, approveId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        if ((userId == null) || (friendId == null) || (!userExistsInTable(userId)) || (!userExistsInTable(friendId))) {
            throw new EntityNotFoundException("user not found");
        }
        SqlRowSet sqlFriendsApproveIdSet = jdbcTemplate.queryForRowSet("SELECT approve_id FROM friends WHERE user_id = ? AND friends_id = ?",
                userId, friendId);
        if (!sqlFriendsApproveIdSet.next()) {
            throw new EntityNotFoundException("friends are not found");
        }
        Long approveId = mapApproveId(sqlFriendsApproveIdSet);
        jdbcTemplate.update("DELETE FROM approved_friends WHERE  approve_id = ?", approveId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        String request = "SELECT u.USER_ID , u.EMAIL , u.LOGIN , u.name, u.BIRTHDAY FROM users AS u INNER JOIN \n" +
                "(SELECT u1.friends_id FROM (SELECT f1.friends_id FROM FRIENDS AS f1 INNER JOIN APPROVED_FRIENDS as af1 ON f1.APPROVE_ID = af1.APPROVE_ID WHERE f1.user_id = ?) AS u1 \n" +
                "INNER JOIN (SELECT f2.friends_id FROM FRIENDS AS f2 INNER JOIN APPROVED_FRIENDS as af2 ON f2.APPROVE_ID = af2.APPROVE_ID WHERE f2.user_id = ?) AS u2 \n" +
                "ON u1.friends_id = u2.friends_id) AS allf\n" +
                "ON  u.user_id = allf.friends_id;";

        return jdbcTemplate.query(request, (rs, rowNum) -> mapUser(rs), userId, friendId)
                .stream()
                .map(x -> {
                    x.setFriends(mapUserFriends(x.getId()));
                    return x; })
                .collect(Collectors.toList());
    }

    @Override
    public Set<User> getFriends(Long userId) {
        if (userId == null || !userExistsInTable(userId)) {
            throw new EntityNotFoundException("user not found");
        }
        String request = "SELECT u.USER_ID , u.EMAIL , u.LOGIN , u.name, u.BIRTHDAY FROM users AS u INNER JOIN " +
                "(SELECT * FROM friends WHERE user_id = ?) f ON u.USER_ID = f.FRIENDS_ID ;";
        return jdbcTemplate.query(request, (rs, rowNum) -> mapUser(rs), userId)
                .stream()
                .map(x -> {
                    x.setFriends(mapUserFriends(x.getId()));
                    return x; })
                .collect(Collectors.toSet());
    }

    @Override
    public void approveFriend(Long id, Long friendId) {
        if ((id == null) || (friendId == null) || (!userExistsInTable(id)) || (!userExistsInTable(friendId))) {
            throw new EntityNotFoundException("user not found");
        }
        Set<User> friends = getFriends(id);
        if (!friends.contains(friendId)) {
            throw new EntityNotFoundException("friendship not found");
        }
        jdbcTemplate.update("UPDATE APPROVED_FRIENDS SET approved = TRUE WHERE approve_id = " +
                "(SELECT APPROVE_ID FROM friends AS f WHERE user_id = ? AND friends_id = ?);", id, friendId);
    }

    private User mapUser(ResultSet rs) throws SQLException {
        long userId = rs.getLong("user_id");
        String userEmail = rs.getString("email");
        String userLogin = rs.getString("login");
        String userName = rs.getString("name");
        LocalDate userBirthday = LocalDate.parse(rs.getString("birthday"));
        User user = new User();
        user.setId(userId);
        user.setEmail(userEmail);
        user.setLogin(userLogin);
        user.setName(userName);
        user.setBirthday(userBirthday);
        return user;
    }

    private User mapUser(SqlRowSet sqlRowSet) {
        User user = new User();
        user.setId(sqlRowSet.getLong("user_id"));
        user.setEmail(sqlRowSet.getString("email"));
        user.setLogin(sqlRowSet.getString("login"));
        user.setName(sqlRowSet.getString("name"));
        user.setBirthday(LocalDate.parse(sqlRowSet.getString("birthday")));
        return user;
    }

    private Set<Long> mapUserFriends(Long userId) {
        String request = "SELECT fr.FRIENDS_ID  FROM users AS f INNER JOIN FRIENDS fr ON f.USER_ID = fr.USER_ID WHERE f.USER_ID = ?";
        return new HashSet<Long>(jdbcTemplate.queryForList(request, Long.class, userId));
    }

    private Long mapApproveId(SqlRowSet sqlRowSet) {
        return sqlRowSet.getLong("approve_id");
    }

    private void userNameAutoCompletion(User user) {
        if ((user.getName() == null) || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }

    private boolean userExistsInTable(Long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);
        return sqlRowSet.next();
    }
}