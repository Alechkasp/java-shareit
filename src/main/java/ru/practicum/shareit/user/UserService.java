package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getById(Integer userId) {
        return userRepository.getById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public CreateUserDto create(CreateUserDto createUserDto) {
        checkEmail(createUserDto.getEmail());
        User newUser = UserMapper.createUserDtoToUser(createUserDto);
        userRepository.create(newUser);
        return UserMapper.createUserDtoFromUser(newUser);
    }

    public UpdateUserDto update(Integer userId, UpdateUserDto updateUserDto) {
        User updateUser = userRepository.getById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));

        if ((updateUserDto.getEmail() != null) && (!updateUserDto.getEmail().isBlank())
                && (!updateUserDto.getEmail().equals(updateUser.getEmail()))) {
            checkEmail(updateUserDto.getEmail());
            updateUser.setEmail(updateUserDto.getEmail());
        }

        if ((updateUserDto.getName() != null) && (!updateUserDto.getName().isBlank())) {
            updateUser.setName(updateUserDto.getName());
        }

        return UserMapper.updateUserDtoFromUser(updateUser);
    }

    public User deleteById(Integer userId) {
        return userRepository.deleteById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));
    }

    private void checkEmail(String email) {
        userRepository.getByEmail(email).ifPresent(user -> {
            throw new DuplicatedEmailException("Такой email уже существует!");
        });
    }
}
