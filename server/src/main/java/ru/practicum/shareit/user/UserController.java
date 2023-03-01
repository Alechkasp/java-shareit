package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDto> getAll(@RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users.");
        return userService.getAll(from, size);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Long userId) {
        log.info("Получен запрос GET /users/{}.", userId);
        return userService.getById(userId);
    }

    @PostMapping()
    public UserDto create(@RequestBody CreateUserDto createUserDto) {
        log.info("Получен запрос POST /users.");
        return userService.create(createUserDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long userId,
                          @RequestBody UpdateUserDto updateUserDto) {
        log.info("Получен запрос PATCH /users/{}.", userId);
        return userService.update(userId, updateUserDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long userId) {
        log.info("Получен запрос DELETE /users/{}.", userId);
        userService.deleteById(userId);
    }
}
