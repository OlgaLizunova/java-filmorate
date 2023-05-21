package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilmById(Long filmId);
    Film deleteFilm(Long filmId);
    Collection<Film> getAllFilms();
}
