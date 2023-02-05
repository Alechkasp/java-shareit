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

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
