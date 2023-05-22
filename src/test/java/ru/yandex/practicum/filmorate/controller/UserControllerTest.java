package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private UserStorage userStorage;
    private FilmStorage filmStorage;
    private UserServiceImpl userService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        userService = new UserServiceImpl(userStorage);
        userController = new UserController(userService);
        user1 = User.builder()
                .id(1L)
                .login("maro")
                .email("maro@mail.ru")
                .birthday(LocalDate.of(1980, 12, 30))
                .build();
        user2 = User.builder()
                .id(2L)
                .name("User2")
                .login("taro 2")
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
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userController.updateUser(user1));
        assertEquals("Пользователя с id=1 не существует", exception.getMessage());
    }
}