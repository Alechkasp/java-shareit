package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Component
public interface UserRepository {
    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();

    UserDto createUser(User user);

    UserDto updateUser(Integer userId, User userUpdate);

    void deleteUserById(Integer userId);
}
