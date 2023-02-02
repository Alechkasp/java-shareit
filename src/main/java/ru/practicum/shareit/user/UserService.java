package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getById(Long userId);

    User create(CreateUserDto createUserDto);

    User update(Long userId, UpdateUserDto updateUserDto);

    User deleteById(Long userId);
}
