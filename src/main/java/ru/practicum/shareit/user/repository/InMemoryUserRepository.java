package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Optional<User> getById(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> deleteById(Integer userId) {
        return Optional.ofNullable(users.remove(userId));
    }
}
