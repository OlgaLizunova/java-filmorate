package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private UserStorage userStorage;
    private UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userController = new UserController(userStorage, userService);
        user1 = User.builder()
                .name("User1")
                .login("maro")
                .email("maro@mail.ru")
                .birthday(LocalDate.of(1980, 12, 30))
                .build();
        user2 = User.builder()
                .name("User2")
                .login("taro")
                .email("taro@mail.ru")
                .birthday(LocalDate.of(1970, 9, 30))
                .build();
    }

    @Test
    void shouldSetUserNameAsLoginThenPostUserWithoutName() {
        userController.postUser(user1);
        assertEquals(user1.getName(), user1.getLogin());
    }

    @Test
    void shouldThrowExceptionThenPostUserWithIncorrectLogin() {
        UserValidationException exception = Assertions.assertThrows(UserValidationException.class,
                () -> userController.postUser(user2));
        assertEquals("Логин не должен содержать пробелы", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionThenUpdateUserWithIncorrectId() {
        UserValidationException exception = Assertions.assertThrows(UserValidationException.class,
                () -> userController.updateUser(user1));
        assertEquals("Пользователя с id=1 не существует", exception.getMessage());
    }
}