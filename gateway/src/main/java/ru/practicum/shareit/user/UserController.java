package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
@Controller
public class UserController {

    private final UserClient userClient;

    @GetMapping()
    public ResponseEntity<Object> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users.");
        return userClient.getAll(from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long userId) {
        log.info("Получен запрос GET /users/{}.", userId);
        return userClient.getById(userId);
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody CreateUserDto createUserDto) {
        log.info("Получен запрос POST /users.");
        return userClient.create(createUserDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long userId,
                          @Valid @RequestBody UpdateUserDto updateUserDto) {
        log.info("Получен запрос PATCH /users/{}.", userId);
        return userClient.update(userId, updateUserDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long userId) {
        log.info("Получен запрос DELETE /users/{}.", userId);
        userClient.deleteById(userId);
    }
}
