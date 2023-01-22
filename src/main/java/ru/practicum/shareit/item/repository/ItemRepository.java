package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> getById(Integer itemId);

    List<Item> getByText(String text);

    List<Item> getByUserId(Integer userId);

    Item create(Item item);

    Item update(Item item);

    Optional<Item> delete(Integer itemId);
}
