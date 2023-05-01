package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film(1, "New film", "Description of New film",
                LocalDate.of(1800, 11, 11), 180);
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