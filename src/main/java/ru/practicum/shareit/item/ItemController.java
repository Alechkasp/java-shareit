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
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;

import ru.practicum.shareit.item.dto.UpdateItemDto;

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
    public Item getById(@PathVariable Long itemId,
                        @RequestHeader(name = HEADER) Long userId) {
        log.info("Получен запрос GET /items/{itemId}. " + itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam(required = false) String text) {
        log.info("Получен запрос GET /items/search.");
        return itemService.search(text);
    }

    @GetMapping
    public List<Item> getByUserId(@RequestHeader(name = HEADER) Long userId) {
        log.info("Получен запрос GET /items.");
        return itemService.getByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item create(@RequestHeader(name = HEADER) Long userId,
                       @Valid @RequestBody CreateItemDto itemDto) {
        log.info("Получен запрос POST /items.");
        return itemService.create(userId, itemDto);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@PathVariable Long id,
                                    @RequestHeader(name = HEADER) Long userId,
                                    @Valid @RequestBody CreateCommentDto commentDto) {
        log.info("Получен запрос POST /items/{id}/comment.");
        return itemService.createComment(id, userId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@PathVariable Long itemId,
                       @RequestHeader(name = HEADER) Long userId,
                       @Valid @RequestBody UpdateItemDto itemDto) {
        log.debug("Получен запрос PATCH /items/{itemId}. " + itemId);
        return itemService.update(itemId, userId, itemDto);
    }

    @DeleteMapping("/{id}")
    public Item delete(@PathVariable Long id) {
        log.debug("Получен запрос DELETE /items/{itemId}. " + id);
        return itemService.delete(id);
    }
}
