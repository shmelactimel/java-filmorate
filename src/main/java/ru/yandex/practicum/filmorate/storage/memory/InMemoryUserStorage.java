package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> mapOfUsers = new HashMap<>();
    private Long id = 0L;

    public List<User> getUsers() {
        return new ArrayList<>(mapOfUsers.values());
    }

    @Override
    public User getUser(Long userId) {
        User user = mapOfUsers.get(userId);
        if (user == null) {
            throw new EntityNotFoundException("user not found");
        }
        return user;
    }

    @Override
    public User addUser(User user) throws EntityAlreadyExistException {
        userNameAutoCompletion(user);
        if ((user.getId() != null) && (mapOfUsers.get(user.getId()) != null)) {
            throw new EntityAlreadyExistException("user already exists");
        }
        user.setId(++id);
        mapOfUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (userId == null) {
            userId = user.getId();
        }
        log.info("update user id: " + userId);
        if (userId == null || (mapOfUsers.get(userId) == null)) {
            throw new EntityNotFoundException("user not found");
        }
        userNameAutoCompletion(user);
        mapOfUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = mapOfUsers.get(userId);
        User friend = mapOfUsers.get(friendId);
        Set<Long> friends = user.getFriends();
        friends.add(friend.getId());
        Set<Long> friendFriends = friend.getFriends();
        friendFriends.add(user.getId());
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = mapOfUsers.get(userId);
        User friend = mapOfUsers.get(friendId);
        Set<Long> friends = user.getFriends();
        friends.remove(friend.getId());
        Set<Long> friendFriends = friend.getFriends();
        friendFriends.remove(user.getId());
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = mapOfUsers.get(userId);
        User friend = mapOfUsers.get(friendId);
        Set<Long> friends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();
        Set<Long> commonFriends = new HashSet<Long>(friends);
        commonFriends.retainAll(friendFriends);
        return commonFriends.stream()
                .map(x -> mapOfUsers.get(x))
                .collect(Collectors.toList());
    }

    @Override
    public Set<User> getFriends(Long userId) {
        User user = mapOfUsers.get(userId);
        Set<User> friends = user.getFriends().stream()
                .map(x -> mapOfUsers.get(x))
                .collect(Collectors.toSet());
        return friends;
    }

    private void userNameAutoCompletion(User user) {
        if ((user.getName() == null) || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void approveFriend(Long id, Long friendId) {
    }
}