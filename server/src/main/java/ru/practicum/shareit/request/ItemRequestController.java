package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getById(@RequestHeader(name = Variables.HEADER) Long userId,
                                  @PathVariable Long requestId) {
        log.info("Получен запрос GET /requests/{}.", requestId);
        return itemRequestService.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(name = Variables.HEADER) Long userId,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /requests/all?from={}&size={}", from, size);
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequester(@RequestHeader(name = Variables.HEADER) Long userId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /requests");
        return itemRequestService.getAllByRequester(userId, from, size);
    }

    @PostMapping
    public ItemRequestDtoShort create(@RequestHeader(name = Variables.HEADER) Long userId,
                                      @RequestBody ItemRequestDtoShort itemRequestDto) {
        log.info("Получен запрос POST /requests.");
        return itemRequestService.create(userId, itemRequestDto);
    }
}
