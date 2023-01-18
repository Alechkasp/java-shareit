package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getById(Integer userId) {
        return userRepository.getById(userId);
    }

    public List<UserDto> getList() {
        return userRepository.getList();
    }

    public UserDto create(User user) {
        return userRepository.create(user);
    }

    public UserDto update(Integer userId, User userUpdate) {
        return userRepository.update(userId, userUpdate);
    }

    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);
    }
}
