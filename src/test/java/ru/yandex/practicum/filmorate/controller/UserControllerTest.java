package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user1 = new User(1, "mail@mail.ru", "newLogin", "",
                LocalDate.of(1980, 10, 10));
        user2 = new User(1, "mail1@mail.ru", "new Login", "",
                LocalDate.of(1980, 10, 10));
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