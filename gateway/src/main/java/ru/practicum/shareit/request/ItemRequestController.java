package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Variables.HEADER;

@Slf4j
@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(name = HEADER) Long userId,
                                          @PathVariable Long requestId) {
        log.info("Получен запрос GET /requests/{}.", requestId);
        return itemRequestClient.getById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(name = HEADER) Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /requests/all?from={}&size={}", from, size);
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequester(@RequestHeader(name = HEADER) Long userId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /requests");
        return itemRequestClient.getAllByRequester(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(name = HEADER) Long userId,
                                         @Valid @RequestBody ItemRequestDtoShort itemRequestDto) {
        log.info("Получен запрос POST /requests.");
        return itemRequestClient.create(userId, itemRequestDto);
    }
}
