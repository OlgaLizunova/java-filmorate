package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder

public class User {

    private Long id;

    @NotBlank(message = "электронная почта не может быть пустой")
    @Email(message = "Некорректный формат электронной почты")
    private String email;

    @NotNull(message = "Укажите логин")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Long> friends;

    public User(Long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        if (friends == null) {
            this.friends = new HashSet<>();
        }
    }
}
