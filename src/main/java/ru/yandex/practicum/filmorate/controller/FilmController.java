package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.Film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmServiceImpl filmService;

    @Autowired
    public FilmController(FilmServiceImpl filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    public ResponseEntity<Film> postFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма {}", film);
        return new ResponseEntity<>(filmService.addFilm(film), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление фильма {}", film);
        return new ResponseEntity<>(filmService.updateFilm(film), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос на добавление лайка пользователя с Id = {} фильму {}", userId, filmService.getFilmById(id));
        filmService.addLike(id, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        log.info("Получен запрос на получение фильма с Id = {}", id);
        return new ResponseEntity<>(filmService.getFilmById(id), HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        log.info("Получен запрос на список {} самых популярных фильмов", count);
        return new ResponseEntity<>(filmService.getPopular(count), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Collection<Film>> getAllFilm() {
        log.info("Получен запрос на список всех фильмов");
        return new ResponseEntity<>(filmService.getAllFilms(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос на удаление лайка пользователя с Id = {} фильму {}", userId, filmService.getFilmById(id));
        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Film> deleteFilm(@PathVariable Long id) {
        log.info("Получен запрос  на удаление фильма с ID={}", id);
        return new ResponseEntity<>(filmService.deleteFilm(id), HttpStatus.OK);
    }
}
