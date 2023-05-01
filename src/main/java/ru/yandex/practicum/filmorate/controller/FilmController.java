package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    private int generatedId() {
        log.info("Создан новый filmId {}", filmId);
        filmId++;
        return filmId;
    }

    @PostMapping()
    public ResponseEntity<Film> postFilm(@Valid @RequestBody Film film) {
        log.info("получен запрос на добавление фильма {}", film);
        if (!isValid(film)) {
            throw new FilmValidationException("Проверьте все поля");
        }
        film.setId(filmId);
        log.info("Фильм {} добавлен в коллекцию", film.getName());
        films.put(filmId, film);
        filmId = generatedId();
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление фильма {}", film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильма с id={} не существует", film.getId());
            throw new FilmValidationException("Фильма с id " + film.getId() + " не существует");
        }
        Film existFilm = films.get(film.getId());
        log.info("Информация о фильме {} изменена на {}", existFilm, film);
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Collection<Film>> getAllFilm() {
        log.info("Получен запрос на список всех фильмов");
        return new ResponseEntity<>(films.values(), HttpStatus.OK);
    }

    public boolean isValid(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не должна быть раньше 28 декабря 1895 года");
            throw new FilmValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
        }
        return true;
    }
}
