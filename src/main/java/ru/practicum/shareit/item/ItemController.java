package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private static final String HEADER = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Integer itemId, @RequestHeader(HEADER) Integer userId) {
        log.info("Получен запрос GET /items/{itemId}");
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getList(@RequestHeader(HEADER) Integer userId) {
        log.info("Получен запрос GET /items");
        return itemService.getList(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(HEADER) Integer userId,
                                     @RequestParam("text") String text) {
        log.info("Получен запрос GET /items/search");
        return itemService.search(userId, text);
    }
    @PostMapping
    public ItemDto create(@Valid @RequestBody Item item, @RequestHeader(HEADER) Integer userId) {
        log.debug("Получен запрос POST /items");
        return itemService.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @PathVariable Integer itemId, @RequestHeader(HEADER) Integer userId,
                              @RequestBody Item itemUpdate) {
        log.debug("Получен запрос PATCH /items/{itemId}");
        return itemService.update(itemId, userId, itemUpdate);
    }
}
