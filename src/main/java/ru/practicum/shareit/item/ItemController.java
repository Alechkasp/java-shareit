package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemServiceImpl;
    private static final String HEADER = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Integer itemId) {
        log.info("Получен запрос GET /items/{itemId}");
        return itemServiceImpl.getById(itemId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam(required = false) String text) {
        log.info("Получен запрос GET /items/search");
        return itemServiceImpl.search(text);
    }

    @GetMapping
    public List<Item> getByUserId(@RequestHeader(HEADER) Integer userId) {
        log.info("Получен запрос GET /items");
        return itemServiceImpl.getByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateItemDto create(@RequestHeader(HEADER) Integer userId,  @Valid @RequestBody CreateItemDto createItemDto) {
        log.debug("Получен запрос POST /items");
        return itemServiceImpl.create(userId, createItemDto);
    }

    @PatchMapping("/{itemId}")
    public UpdateItemDto update(@PathVariable Integer itemId, @RequestHeader(HEADER) Integer userId,
                                @Valid @RequestBody UpdateItemDto updateItemDto) {
        log.debug("Получен запрос PATCH /items/{itemId}");
        return itemServiceImpl.update(itemId, userId, updateItemDto);
    }

    @DeleteMapping("/{id}")
    public Item delete(@PathVariable Integer itemId) {
        log.debug("Получен запрос DELETE /items/{itemId}");
        return itemServiceImpl.delete(itemId);
    }
}
