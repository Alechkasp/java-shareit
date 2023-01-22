package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {
    public static User createUserDtoToUser(CreateUserDto createUserDto) {
        return new User(
                createUserDto.getId(),
                createUserDto.getName(),
                createUserDto.getEmail()
        );
    }

    public static User updateUserDtoToUser(UpdateUserDto updateUserDto) {
        return new User(
                updateUserDto.getId(),
                updateUserDto.getName(),
                updateUserDto.getEmail()
        );
    }

    public static CreateUserDto createUserDtoFromUser(User createUser) {
        return new CreateUserDto(
                createUser.getId(),
                createUser.getName(),
                createUser.getEmail()
        );
    }

    public static UpdateUserDto updateUserDtoFromUser(User updateUser) {
        return new UpdateUserDto(
                updateUser.getId(),
                updateUser.getName(),
                updateUser.getEmail()
        );
    }
}
