package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private static final String HEADER = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Integer itemId, @RequestHeader(HEADER) Integer userId) {
        log.info("Получен запрос GET /items/{itemId}");
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(HEADER) Integer userId) {
        log.info("Получен запрос GET /items");
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(HEADER) Integer userId,
                                     @RequestParam("text") String text) {
        log.info("Получен запрос GET /items/search");
        return itemService.searchItems(userId, text);
    }
    @PostMapping
    public ItemDto createItem(@Valid @RequestBody Item item, @RequestHeader(HEADER) Integer userId) {
        log.debug("Получен запрос POST /items");
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @PathVariable Integer itemId, @RequestHeader(HEADER) Integer userId,
                              @RequestBody Item itemUpdate) {
        log.debug("Получен запрос PATCH /items/{itemId}");
        return itemService.updateItem(itemId, userId, itemUpdate);
    }
}
