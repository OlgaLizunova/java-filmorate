package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private FilmController filmController;
    private FilmStorage filmStorage;
    private FilmService filmService;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController(filmStorage, filmService);
        film = Film.builder()
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
        FilmValidationException exception = Assertions.assertThrows(FilmValidationException.class,
                () -> filmController.updateFilm(film));
        assertEquals("Фильма с id 1 не существует", exception.getMessage());
    }
}