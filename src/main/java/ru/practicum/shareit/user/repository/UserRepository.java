package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository {
    Optional<User> getById(Integer userId);

    Optional<User> getByEmail(String email);

    List<User> getAll();

    User create(User user);

    void update(User user);

    Optional<User> deleteById(Integer userId);
}
