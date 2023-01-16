package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {
    int id = 0;
    private static final Map<Integer, Item> items = new HashMap<>();

    @Override
    public ItemDto getItemById(Integer itemId, Integer userId) {
        if (checkItem(itemId)) {
            return ItemMapper.toItemDto(items.get(itemId));
        }
        throw new ItemNotFoundException("Вещь не найдена!");
    }

    @Override
    public List<ItemDto> getItems(Integer userId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items.values()) {
            if (i.getOwner().getId().equals(userId)) {
                itemsDto.add(ItemMapper.toItemDto(i));
            }
        }
        return itemsDto;
    }

    @Override
    public ItemDto createItem(Item item, Integer userId) {
        if (!InMemoryUserRepository.checkUser(userId)) {
            log.error("Пользователь не найден!");
            throw new UserNotFoundException("Пользователь не найден!");
        }
        if (item.getAvailable() == null) {
            log.error("Вещь не может быть добавлена!");
            throw new UnavailableException("Вещь не может быть добавлена!");
        }
        id++;
        item.setId(id);
        item.setOwner(InMemoryUserRepository.getUsers().get(userId));
        items.put(item.getId(), item);
        log.info("Вещь создана!");
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Integer itemId, Integer userId, Item itemUpdate) {
        if (checkItem(itemId)) {
            Item item = items.get(itemId);
            if (!checkOwner(item, userId)) {
                log.error("Владелец вещи не найден!");
                throw new UserNotFoundException("Владелец вещи не найден!");
            }
            if (itemUpdate.getName() != null)
                item.setName(itemUpdate.getName());
            if (itemUpdate.getDescription() != null)
                item.setDescription(itemUpdate.getDescription());
            if (itemUpdate.getAvailable() != null)
                item.setAvailable(itemUpdate.getAvailable());
            items.put(itemId, item);
            log.info("Вещь с id = {} обновлена!", itemId);
            return ItemMapper.toItemDto(item);
        }
        log.error("Вещь не найдена!");
        throw new ItemNotFoundException("Вещь не найдена!");
    }


    @Override
    public List<ItemDto> searchItems(Integer userId, String text) {
        List<ItemDto> foundItems = new ArrayList<>();
        if (!text.isBlank()) {
            String query = text.toLowerCase();
            for (Item i : items.values()) {
                if ((i.getName().toLowerCase().contains(query) || i.getDescription().toLowerCase().contains(query))
                        && i.getAvailable()) {
                    foundItems.add(ItemMapper.toItemDto(i));
                }
            }
        }
        return foundItems;
    }

    private boolean checkItem(Integer itemId) {
        return items.containsKey(itemId);
    }

    private boolean checkOwner(Item item, Integer userId) {
        return item.getOwner().getId().equals(userId);
    }
}