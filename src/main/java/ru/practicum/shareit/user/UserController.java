package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<User> getAll() {
        log.info("Получен запрос GET /users.");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос GET /users/{id}");
        return userService.getById(userId);
    }

    @PatchMapping("/{id}")
    public UpdateUserDto update(@PathVariable("id") Integer userId, @Valid @RequestBody UpdateUserDto updateUserDto) {
        log.debug("Получен запрос PATCH /users/{id}");
        return userService.update(userId, updateUserDto);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserDto create(@Valid @RequestBody CreateUserDto createUserDto) {
        log.debug("Получен запрос POST /users");
        return userService.create(createUserDto);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос DELETE /users/{id}");
        return userService.deleteById(userId);
    }
}
