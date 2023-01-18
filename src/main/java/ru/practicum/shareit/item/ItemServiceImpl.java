package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getById(Integer itemId, Integer userId) {
        return itemRepository.getById(itemId, userId);
    }

    @Override
    public List<ItemDto> getList(Integer userId) {
        return itemRepository.getList(userId);
    }

    @Override
    public ItemDto create(Item item, Integer userId) {
        return itemRepository.create(item, userId);
    }

    @Override
    public ItemDto update(Integer itemId, Integer userId, Item itemUpdate) {
        return itemRepository.update(itemId, userId, itemUpdate);
    }

    @Override
    public List<ItemDto> search(Integer userId, String text) {
        return itemRepository.search(userId, text);
    }
}
