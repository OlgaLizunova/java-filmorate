package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public User deleteUser(Long userId) {
        return userStorage.deleteUser(userId);
    }

    @Override
    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        List<User> friends = new ArrayList<>();
        if (user.getFriends() != null) {
            for (Long id : user.getFriends()) {
                friends.add(userStorage.getUserById(id));
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        Set<Long> intersection = new HashSet<>(firstUser.getFriends());
        intersection.retainAll(secondUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Long id : intersection) {
            commonFriends.add(userStorage.getUserById(id));
        }
        return commonFriends;
    }
}
