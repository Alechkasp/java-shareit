package ru.practicum.shareit.item;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.comment.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Variables.HEADER;

@Slf4j
@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable Long itemId,
                                          @RequestHeader(name = HEADER, required = false) Long userId) {
        log.info("Получен запрос GET /items/{}.", itemId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /items/search?from={}&size={}.", from, size);
        return itemClient.search(text, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader(name = HEADER) Long userId,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /items?from={}&size={}.", from, size);
        return itemClient.getByUserId(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(name = HEADER) Long userId,
                                         @Valid @RequestBody CreateItemDto itemDto) {
        log.info("Получен запрос POST /items.");
        return itemClient.create(userId, itemDto);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long id,
                                                @RequestHeader(name = HEADER) Long userId,
                                                @Valid @RequestBody CreateCommentDto commentDto) {
        log.info("Получен запрос POST /items/{id}/comment.");
        return itemClient.createComment(id, userId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId,
                                         @RequestHeader(name = HEADER) Long userId,
                                         @Valid @RequestBody UpdateItemDto itemDto) {
        log.debug("Получен запрос PATCH /items/{}.", itemId);
        return itemClient.update(itemId, userId, itemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.debug("Получен запрос DELETE /items/{}.", id);
        return itemClient.delete(id);
    }
}
