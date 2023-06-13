package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private FilmController filmController;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private FilmServiceImpl filmService;
    private Film film;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmServiceImpl(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        film = Film.builder()
                .id(1L)
                .name("New film")
                .description("Description of New film")
                .releaseDate(LocalDate.of(1800, 11, 11))
                .duration(180)
                .build();
    }

    @Test
    void shouldThrowExceptionThenPostFilmWithIncorrectDate() {
        FilmValidationException exception = Assertions.assertThrows(FilmValidationException.class,
                () -> filmController.postFilm(film));
        assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionThenUpdateFilmWithIncorrectId() {
        FilmNotFoundException exception = Assertions.assertThrows(FilmNotFoundException.class,
                () -> filmController.updateFilm(film));
        assertEquals("Фильма с id 1 не существует", exception.getMessage());
    }
}