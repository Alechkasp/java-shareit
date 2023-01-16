package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public UserDto createUser(User user) {
        return userRepository.createUser(user);
    }

    public UserDto updateUser(Integer userId, User userUpdate) {
        return userRepository.updateUser(userId, userUpdate);
    }

    public void deleteUserById(Integer userId) {
        userRepository.deleteUserById(userId);
    }
}
