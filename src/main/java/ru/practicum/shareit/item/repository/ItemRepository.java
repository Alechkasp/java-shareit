package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Component
public interface ItemRepository {
    ItemDto getById(Integer itemId, Integer userId);

    List<ItemDto> getList(Integer userId);

    ItemDto create(Item item, Integer userId);

    ItemDto update(Integer itemId, Integer userId, Item itemUpdate);

    List<ItemDto> search(Integer userId, String text);
}
