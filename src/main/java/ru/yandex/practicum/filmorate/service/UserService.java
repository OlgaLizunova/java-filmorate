package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserService {
    User addUser(User user);

    User updateUser(User user);

    User deleteUser(Long userId);

    User getUserById(Long userId);

    Collection<User> getUsers();

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId);
}
