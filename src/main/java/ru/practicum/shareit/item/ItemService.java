package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Component
public interface ItemService {
    ItemDto getItemById(Integer itemId, Integer userId);

    List<ItemDto> getItems(Integer userId);

    ItemDto createItem(Item item, Integer userId);

    ItemDto updateItem(Integer itemId, Integer userId, Item itemUpdate);

    List<ItemDto> searchItems(Integer userId, String text);
}
