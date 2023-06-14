package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final FilmService filmService;
	private final UserService userService;
	private User firstUser;
	private User secondUser;
	private User thirdUser;
	private Film firstFilm;
	private Film secondFilm;
	private Film thirdFilm;

	@BeforeEach
	public void beforeEach() {
		firstUser = User.builder()
				.name("User1")
				.login("First")
				.email("user1@mail.ru")
				.birthday(LocalDate.of(1990, 01, 10))
				.build();

		secondUser = User.builder()
				.name("User2")
				.login("Second")
				.email("user2@mail.ru")
				.birthday(LocalDate.of(1990, 02, 10))
				.build();

		thirdUser = User.builder()
				.name("User3")
				.login("Third")
				.email("user3@mail.ru")
				.birthday(LocalDate.of(1990, 03, 10))
				.build();

		firstFilm = Film.builder()
				.name("Москва слезам не верит")
				.description("Советский мелодраматический фильм режиссёра Владимира Меньшова, лидер проката 1980 года в СССР")
				.releaseDate(LocalDate.of(1980, 02, 11))
				.duration(148)
				.build();
		firstFilm.setMpa(new Mpa(1, "G"));
		firstFilm.setLikes(new HashSet<>());
		firstFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),
				new Genre(1, "Комедия"))));

		secondFilm = Film.builder()
				.name("Pretty Woman")
				.description("американская комедийная мелодрама режиссёра Гарри Маршалла с Ричардом Гиром " +
						"и Джулией Робертс в главных ролях.")
				.releaseDate(LocalDate.of(1990, 03, 23))
				.duration(119)
				.build();
		secondFilm.setMpa(new Mpa(3, "PG-13"));
		secondFilm.setLikes(new HashSet<>());
		secondFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"))));

		thirdFilm = Film.builder()
				.name("Avatar")
				.description("American epic science fiction film directed, written, produced, and co-edited" +
						" by James Cameron. It is set in the mid-22nd century when humans are colonizing Pandora...")
				.releaseDate(LocalDate.of(2009, 12, 10))
				.duration(162)
				.build();
		thirdFilm.setMpa(new Mpa(3, "PG-13"));
		thirdFilm.setLikes(new HashSet<>());
		thirdFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(6, "Боевик"))));
	}

	@Test
	public void testCreateUserAndGetUserById() {
		firstUser = userStorage.addUser(firstUser);
		Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(firstUser.getId()));
		assertThat(userOptional)
				.hasValueSatisfying(user ->
						assertThat(user)
								.hasFieldOrPropertyWithValue("id", firstUser.getId())
								.hasFieldOrPropertyWithValue("name", "User1"));
	}

	@Test
	public void testGetUsers() {
		firstUser = userStorage.addUser(firstUser);
		secondUser = userStorage.addUser(secondUser);
		List<User> listUsers = userStorage.getUsers();
		assertThat(listUsers).contains(firstUser);
		assertThat(listUsers).contains(secondUser);
	}

	@Test
	public void testUpdateUser() {
		firstUser = userStorage.addUser(firstUser);
		User updateUser = User.builder()
				.id(firstUser.getId())
				.name("UpdateUser1")
				.login("First")
				.email("user1@mail.ru")
				.birthday(LocalDate.of(1990, 01, 10))
				.build();
		Optional<User> testUpdateUser = Optional.ofNullable(userStorage.updateUser(updateUser));
		assertThat(testUpdateUser)
				.hasValueSatisfying(user -> assertThat(user)
						.hasFieldOrPropertyWithValue("name", "UpdateUser1")
				);
	}

	@Test
	public void deleteUser() {
		firstUser = userStorage.addUser(firstUser);
		userStorage.deleteUser(firstUser.getId());
		List<User> listUsers = userStorage.getUsers();
		assertThat(listUsers).hasSize(0);
	}

	@Test
	public void testCreateFilmAndGetFilmById() {
		firstFilm = filmStorage.addFilm(firstFilm);
		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(firstFilm.getId()));
		assertThat(filmOptional)
				.hasValueSatisfying(film -> assertThat(film)
						.hasFieldOrPropertyWithValue("id", firstFilm.getId())
						.hasFieldOrPropertyWithValue("name", "Москва слезам не верит")
				);
	}

	@Test
	public void testGetFilms() {
		firstFilm = filmStorage.addFilm(firstFilm);
		secondFilm = filmStorage.addFilm(secondFilm);
		thirdFilm = filmStorage.addFilm(thirdFilm);
		List<Film> listFilms = filmStorage.getAllFilms();
		assertThat(listFilms).contains(firstFilm);
		assertThat(listFilms).contains(secondFilm);
		assertThat(listFilms).contains(thirdFilm);
	}

	@Test
	public void testUpdateFilm() {
		firstFilm = filmStorage.addFilm(firstFilm);
		Film updateFilm = Film.builder()
				.id(firstFilm.getId())
				.name("UpdateName")
				.description("UpdateDescription")
				.releaseDate(LocalDate.of(1980, 02, 11))
				.duration(148)
				.build();
		updateFilm.setMpa(new Mpa(1, "G"));
		Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.updateFilm(updateFilm));
		assertThat(testUpdateFilm)
				.hasValueSatisfying(film ->
						assertThat(film)
								.hasFieldOrPropertyWithValue("name", "UpdateName")
								.hasFieldOrPropertyWithValue("description", "UpdateDescription")
				);
	}

	@Test
	public void deleteFilm() {
		firstFilm = filmStorage.addFilm(firstFilm);
		secondFilm = filmStorage.addFilm(secondFilm);
		filmStorage.deleteFilm(firstFilm.getId());
		List<Film> listFilms = filmStorage.getAllFilms();
		assertThat(listFilms).hasSize(1);
		assertThat(Optional.of(listFilms.get(0)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Pretty Woman"));
	}

	@Test
	public void testAddLike() {
		firstUser = userStorage.addUser(firstUser);
		firstFilm = filmStorage.addFilm(firstFilm);
		filmService.addLike(firstFilm.getId(), firstUser.getId());
		firstFilm = filmStorage.getFilmById(firstFilm.getId());
		assertThat(firstFilm.getLikes()).hasSize(1);
		assertThat(firstFilm.getLikes()).contains(firstUser.getId());
	}

	@Test
	public void testDeleteLike() {
		firstUser = userStorage.addUser(firstUser);
		secondUser = userStorage.addUser(secondUser);
		firstFilm = filmStorage.addFilm(firstFilm);
		filmService.addLike(firstFilm.getId(), firstUser.getId());
		filmService.addLike(firstFilm.getId(), secondUser.getId());
		filmService.deleteLike(firstFilm.getId(), firstUser.getId());
		firstFilm = filmStorage.getFilmById(firstFilm.getId());
		assertThat(firstFilm.getLikes()).hasSize(1);
		assertThat(firstFilm.getLikes()).contains(secondUser.getId());
	}

	@Test
	public void testGetPopularFilms() {
		firstUser = userStorage.addUser(firstUser);
		secondUser = userStorage.addUser(secondUser);
		thirdUser = userStorage.addUser(thirdUser);

		firstFilm = filmStorage.addFilm(firstFilm);
		filmService.addLike(firstFilm.getId(), firstUser.getId());
		filmService.addLike(firstFilm.getId(), secondUser.getId());

		secondFilm = filmStorage.addFilm(secondFilm);
		filmService.addLike(secondFilm.getId(), firstUser.getId());
		filmService.addLike(secondFilm.getId(), secondUser.getId());
		filmService.addLike(secondFilm.getId(), thirdUser.getId());

		thirdFilm = filmStorage.addFilm(thirdFilm);
		filmService.addLike(thirdFilm.getId(), firstUser.getId());

		List<Film> listFilms = filmService.getPopular(5);

		assertThat(listFilms).hasSize(3);

		assertThat(Optional.of(listFilms.get(0)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Pretty Woman"));

		assertThat(Optional.of(listFilms.get(1)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Москва слезам не верит"));

		assertThat(Optional.of(listFilms.get(2)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Avatar"));
	}

	@Test
	public void testAddFriend() {
		firstUser = userStorage.addUser(firstUser);
		secondUser = userStorage.addUser(secondUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
		assertThat(userService.getFriends(firstUser.getId())).contains(secondUser);
	}

	@Test
	public void testDeleteFriend() {
		firstUser = userStorage.addUser(firstUser);
		secondUser = userStorage.addUser(secondUser);
		thirdUser = userStorage.addUser(thirdUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		userService.addFriend(firstUser.getId(), thirdUser.getId());
		userService.deleteFriend(firstUser.getId(), secondUser.getId());
		assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
		assertThat(userService.getFriends(firstUser.getId())).contains(thirdUser);
	}

	@Test
	public void testGetFriends() {
		firstUser = userStorage.addUser(firstUser);
		secondUser = userStorage.addUser(secondUser);
		thirdUser = userStorage.addUser(thirdUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		userService.addFriend(firstUser.getId(), thirdUser.getId());
		assertThat(userService.getFriends(firstUser.getId())).hasSize(2);
		assertThat(userService.getFriends(firstUser.getId())).contains(secondUser, thirdUser);
	}

	@Test
	public void testGetCommonFriends() {
		firstUser = userStorage.addUser(firstUser);
		secondUser = userStorage.addUser(secondUser);
		thirdUser = userStorage.addUser(thirdUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		userService.addFriend(firstUser.getId(), thirdUser.getId());
		userService.addFriend(secondUser.getId(), firstUser.getId());
		userService.addFriend(secondUser.getId(), thirdUser.getId());
		assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).hasSize(1);
		assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId()))
				.contains(thirdUser);
	}
}
