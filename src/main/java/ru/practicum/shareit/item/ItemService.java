package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public Item getById(Integer itemId) {
        return itemRepository.getById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));
    }

    public List<Item> search(String text) {
        return itemRepository.getByText(text);
    }

    public List<Item> getByUserId(Integer userId) {
        return itemRepository.getByUserId(userId);
    }

    public CreateItemDto create(Integer userId, CreateItemDto createItemDto) {
        User user = userService.getById(userId);
        Item newItem = ItemMapper.createItemDtoToItem(createItemDto);
        newItem.setOwner(user);
        itemRepository.create(newItem);
        return ItemMapper.createItemDtoFromItem(newItem);
    }

    public UpdateItemDto update(Integer itemId, Integer userId, UpdateItemDto updateItemDto) {
        User user = userService.getById(userId);
        Item updateItem = itemRepository.getById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));

        if (!user.equals(updateItem.getOwner())) {
            throw new ItemNotFoundException("Такой вещи нет!");
        }

        if (((updateItemDto.getName()) != null) && (!updateItemDto.getName().isBlank())) {
            updateItem.setName(updateItemDto.getName());
        }

        if (((updateItemDto.getDescription() != null) && (!updateItemDto.getDescription().isBlank()))) {
            updateItem.setDescription(updateItemDto.getDescription());
        }

        if (updateItemDto.getAvailable() != null) {
            updateItem.setAvailable(updateItemDto.getAvailable());
        }
        itemRepository.update(updateItem);

        return ItemMapper.updateItemDtoFromItem(updateItem);
    }

    public Item delete(Integer itemId) {
        return itemRepository.delete(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));
    }
}
