package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long filmId = 1;

    private long generatedId() {
        log.info("Создан новый filmId {}", filmId);
        filmId++;
        return filmId;
    }

    @Override
    public Film addFilm(Film film) {
        if (!isValid(film)) {
            throw new ValidationException("Проверьте все поля");
        }
        film.setId(filmId);
        log.info("Фильм {} добавлен в коллекцию", film.getName());
        films.put(filmId, film);
        filmId = generatedId();
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!films.containsKey(film.getId())) {
            log.error("Фильма с id={} не существует", film.getId());
            throw new FilmNotFoundException("Фильма с id " + film.getId() + " не существует");
        }
        Film existFilm = films.get(film.getId());
        log.info("Информация о фильме {} изменена на {}", existFilm, film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильма с id={} не существует", filmId);
            throw new FilmNotFoundException("Фильма с id " + filmId + " не существует");
        }
        return films.get(filmId);
    }

    @Override
    public Film deleteFilm(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!films.containsKey(filmId)) {
            log.error("Фильма с id={} не существует", filmId);
            throw new FilmNotFoundException("Фильма с id " + filmId + " не существует");
        }
        return films.remove(filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получен запрос на список всех фильмов");
        return new ArrayList<>(films.values());
    }

    public boolean isValid(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не должна быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
        }
        return true;
    }
}
