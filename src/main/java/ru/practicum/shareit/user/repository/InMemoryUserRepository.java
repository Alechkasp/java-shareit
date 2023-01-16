package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {
    int id = 0;
    private static final Map<Integer, User> users = new HashMap<>();

    @Override
    public UserDto getUserById(Integer userId) {
        if (checkUser(userId)) {
            return UserMapper.toUserDto(users.get(userId));
        } else {
            log.error("Пользователь не найден!");
            throw new UserNotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users.values()) {
            usersDto.add(UserMapper.toUserDto(u));
        }
        return usersDto;
    }

    @Override
    public UserDto createUser(User user) {
        if (checkEmail(user)) {
            log.error("Пользователь с таким email уже существует!");
            throw new ValidationException("Пользователь с таким email уже существует!");
        }
        id++;
        user.setId(id);
        users.put(user.getId(), user);
        log.info("Пользователь создан!");
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Integer userId, User userUpdate) {
        if (checkUser(userId)) {
            User user = users.get(userId);
            if (checkEmail(userUpdate)) {
                log.error("Такой email уже существует!");
                throw new ValidationException("Такой email уже существует!");
            }
            if (userUpdate.getName() != null)
                user.setName(userUpdate.getName());
            if (userUpdate.getEmail() != null)
                user.setEmail(userUpdate.getEmail());
            users.put(userId, user);
            log.info("Пользователь с id = {} обновлен!", userId);
            return UserMapper.toUserDto(user);
        } else {
            log.error("Пользователь не найден!");
            throw new UserNotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public void deleteUserById(Integer userId) {
        users.remove(userId);
        log.info("Пользователь удален!");
    }

    public static boolean checkUser(Integer userId) {
        return users.containsKey(userId);
    }

    public static Map<Integer, User> getUsers() {
        return users;
    }

    private boolean checkEmail(User user) {
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
