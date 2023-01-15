package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto getItemById(Integer itemId, Integer userId) {
        return itemRepository.getItemById(itemId, userId);
    }

    @Override
    public List<ItemDto> getItems(Integer userId) {
        return itemRepository.getItems(userId);
    }

    @Override
    public ItemDto createItem(Item item, Integer userId) {
        return itemRepository.createItem(item, userId);
    }

    @Override
    public ItemDto updateItem(Integer itemId, Integer userId, Item itemUpdate) {
        return itemRepository.updateItem(itemId, userId, itemUpdate);
    }

    @Override
    public List<ItemDto> searchItems(Integer userId, String text) {
        return itemRepository.searchItems(userId, text);
    }
}
