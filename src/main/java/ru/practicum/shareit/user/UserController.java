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
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDto> getList() {
        log.info("Получен запрос GET /users.");
        return userService.getList();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос GET /users/{id}");
        return userService.getById(userId);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Integer userId, @RequestBody User userUpdate) {
        log.debug("Получен запрос PATCH /users/{id}");
        return userService.update(userId, userUpdate);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users");
        return userService.create(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос DELETE /users/{id}");
        userService.deleteById(userId);
    }
}
