package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));
    }

    @Override
    public User create(CreateUserDto createUserDto) {
        User newUser = UserMapper.createUserDtoToUser(createUserDto);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User update(Long userId, UpdateUserDto updateUserDto) {
        User updateUser = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));

        if ((updateUserDto.getEmail() != null) && (!updateUserDto.getEmail().isBlank())
                && (!updateUserDto.getEmail().equals(updateUser.getEmail()))) {
            checkEmail(updateUserDto.getEmail());
            updateUser.setEmail(updateUserDto.getEmail());
        }

        if ((updateUserDto.getName() != null) && (!updateUserDto.getName().isBlank())) {
            updateUser.setName(updateUserDto.getName());
        }
        userRepository.save(updateUser);

        return updateUser;
    }

    @Override
    public User deleteById(Long userId) {
        User delUser = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));
        userRepository.deleteById(userId);
        return delUser;
    }

    private void checkEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new DuplicatedEmailException("Такой email уже существует!");
        });
    }
}
