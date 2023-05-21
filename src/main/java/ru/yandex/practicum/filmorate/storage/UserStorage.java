package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    User deleteUser(Long userId);
    User getUserById(Long userId);
    Collection<User> getUsers();
}
