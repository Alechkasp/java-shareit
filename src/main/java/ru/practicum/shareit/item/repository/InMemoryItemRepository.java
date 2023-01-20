package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {
    private Integer id = 0;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Optional<Item> getById(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return items.values()
                .stream()
                .filter(item -> isItemAvailableAndContainsText(item, text))
                .collect(Collectors.toList());

    }

    @Override
    public List<Item> getByUserId(Integer userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        id++;
        item.setId(id);
        items.put(item.getId(), item);
        log.info("Вещь создана!");
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        log.info("Вещь обновлена!");
        return item;
    }

    @Override
    public Optional<Item> delete(Integer itemId) {
        return Optional.ofNullable(items.remove(itemId));
    }

    private boolean isItemAvailableAndContainsText(Item item, String text) {
        boolean isNameContainsText = item.getName().toLowerCase().contains(text.toLowerCase());
        boolean isDescriptionContainsText = item.getDescription().toLowerCase().contains(text.toLowerCase());

        return item.getAvailable() && (isNameContainsText || isDescriptionContainsText);
    }
}
