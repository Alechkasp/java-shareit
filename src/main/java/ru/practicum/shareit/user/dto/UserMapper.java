package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User createUserDtoToUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setId(createUserDto.getId());
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        return user;
    }

    public static User updateUserDtoToUser(UpdateUserDto updateUserDto) {
        User user = new User();
        user.setId(updateUserDto.getId());
        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());
        return user;
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
