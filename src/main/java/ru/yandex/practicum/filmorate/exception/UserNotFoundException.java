package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
    @Getter
    private String message;
}
