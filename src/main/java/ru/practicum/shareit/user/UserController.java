package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос GET /users.");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос GET /users/{id}");
        return userService.getUserById(userId);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") Integer userId, @RequestBody User userUpdate) {
        log.debug("Получен запрос PATCH /users/{id}");
        return userService.updateUser(userId, userUpdate);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users");
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос DELETE /users/{id}");
        userService.deleteUserById(userId);
    }
}
