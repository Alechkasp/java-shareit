package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static Item createItemDtoToItem(CreateItemDto createItemDto) {
        return new Item(
                createItemDto.getId(),
                createItemDto.getName(),
                createItemDto.getDescription(),
                createItemDto.getAvailable(),
                null,
                null,
                null,
                null
        );

    }

    public static Item updateItemDtoToItem(UpdateItemDto updateItemDto) {
        return new Item(
                updateItemDto.getId(),
                updateItemDto.getName(),
                updateItemDto.getDescription(),
                updateItemDto.getAvailable(),
                null,
                null,
                null,
                null
        );
    }

    public static CreateItemDto createItemDtoFromItem(Item createItem) {
        return new CreateItemDto(
                createItem.getId(),
                createItem.getName(),
                createItem.getDescription(),
                createItem.getAvailable()
        );

    }

    public static UpdateItemDto updateItemDtoFromItem(Item updateItem) {
        return new UpdateItemDto(
                updateItem.getId(),
                updateItem.getName(),
                updateItem.getDescription(),
                updateItem.getAvailable()
        );
    }
}