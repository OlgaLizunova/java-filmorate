package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long userId = 1;

    private long generatedId() {
        log.info("Создан новый userId {}", userId);
        userId++;
        return userId;
    }

    @Override
    public User addUser(User user) {
        if (!isValid(user)) {
            throw new UserValidationException("Некорректные данные");
        }
        user.setId(userId);
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            log.info("Поле name пустое, в качестве имени установлен login={}", user.getLogin());
            user.setName(user.getLogin());
        }
        log.info("Пользователь с логином {} добавлен в коллекцию", user.getLogin());
        users.put(userId, user);
        userId = generatedId();
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new UserValidationException("Передан пустой аргумент!");
        }
        if (!users.containsKey(user.getId())) {
            log.error("Пользователя с id={} не существует", user.getId());
            throw new UserNotFoundException("Пользователя с id=" + user.getId() + " не существует");
        }
        User existUser = users.get(user.getId());
        log.info("Информация о пользователе {} изменена на {}", existUser, user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Long userId) {
        if (userId == null) {
            throw new UserValidationException("Передан пустой аргумент!");
        }
        if (!users.containsKey(userId)) {
            log.error("Пользователя с id={} не существует", userId);
            throw new UserNotFoundException("Пользователя с id=" + userId + " не существует");
        }
        return users.remove(userId);
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            log.error("Пользователя с id={} не существует", userId);
            throw new UserNotFoundException("Пользователя с id=" + userId + " не существует");
        }
        return users.get(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    public boolean isValid(User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Логин некорректен", user.getLogin());
            throw new UserValidationException("Логин не должен содержать пробелы");
        }
        return true;
    }
}
