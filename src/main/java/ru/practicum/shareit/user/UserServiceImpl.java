package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    @Transactional(readOnly = true)
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));
    }

    @Transactional
    @Override
    public User create(CreateUserDto createUserDto) {
        User newUser = UserMapper.createUserDtoToUser(createUserDto);
        userRepository.save(newUser);
        return newUser;
    }

    @Transactional
    @Override
    public User update(Long userId, UpdateUserDto updateUserDto) {
        User updateUser = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));

        if ((updateUserDto.getEmail() != null) && (!updateUserDto.getEmail().isBlank())
                && (!updateUserDto.getEmail().equals(updateUser.getEmail()))) {
            updateUser.setEmail(updateUserDto.getEmail());
        }

        if ((updateUserDto.getName() != null) && (!updateUserDto.getName().isBlank())) {
            updateUser.setName(updateUserDto.getName());
        }

        return updateUser;
    }

    @Transactional
    @Override
    public User deleteById(Long userId) {
        User delUser = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));
        userRepository.deleteById(userId);
        return delUser;
    }
}
