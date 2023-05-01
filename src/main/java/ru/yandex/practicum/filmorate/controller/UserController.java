package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    private int generatedId() {
        log.info("Создан новый userId {}", userId);
        userId++;
        return userId;
    }

    @PostMapping()
    public ResponseEntity<User> postUser(@Valid @RequestBody User user) {
        log.info("получен запрос на добавление нового пользователя {}", user);
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
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление данных пользователя {}", user);
        if (!users.containsKey(user.getId())) {
            log.error("Пользователя с id={} не существует", user.getId());
            throw new UserValidationException("Пользователя с id=" + user.getId() + " не существует");
        }
        User existUser = users.get(user.getId());
        log.info("Информация о пользователе {} изменена на {}", existUser, user);
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Collection<User>> getAllUser() {
        log.info("Получен запрос на список всех пользователей");
        return new ResponseEntity<>(users.values(), HttpStatus.OK);
    }

    public boolean isValid(User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Логин некорректен", user.getLogin());
            throw new UserValidationException("Логин не должен содержать пробелы");
        }
        return true;
    }
}
