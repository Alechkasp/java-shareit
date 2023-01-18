package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Component
public interface UserRepository {
    UserDto getById(Integer userId);

    List<UserDto> getList();

    UserDto create(User user);

    UserDto update(Integer userId, User userUpdate);

    void deleteById(Integer userId);
}
