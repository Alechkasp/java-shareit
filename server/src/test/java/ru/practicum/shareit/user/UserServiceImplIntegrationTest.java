package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplIntegrationTest {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private Long userId = 1L;

    @BeforeEach
    void beforeEach() {
        User user = new User(1L, "Alex", "alex.b@yandex.ru");
        userId = userRepository.save(user).getId();
    }

    @Test
    void getAll() {
        List<UserDto> actualDtoList = userService.getAll(0, 10);
        System.out.println(actualDtoList.get(0).getId());

        assertEquals(1, actualDtoList.size());
    }

    @Test
    void getById() {
        UserDto actual = userService.getById(userId);

        assertEquals(userId, actual.getId());
        assertEquals("alex.b@yandex.ru", actual.getEmail());
        assertEquals("Alex", actual.getName());
    }

    @Test
    void create_shouldSavedUserDto() {
        CreateUserDto createUserDto = new CreateUserDto(null, "Bill", "bill.d@yandex.ru");

        UserDto actual = userService.create(createUserDto);

        assertEquals(userId + 1L, actual.getId());
        assertEquals("bill.d@yandex.ru", actual.getEmail());
        assertEquals("Bill", actual.getName());
    }

    @Test
    void update() {
        UpdateUserDto updateUserDto = new UpdateUserDto(null, "Bill", "bill.d@yandex.ru");

        UserDto expected = new UserDto(userId, "Bill", "bill.d@yandex.ru");
        UserDto actual = userService.update(userId, updateUserDto);

        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        userService.deleteById(userId);
        List<UserDto> actualDtoList = userService.getAll(0, 10);

        assertEquals(List.of(), actualDtoList);
    }
}
