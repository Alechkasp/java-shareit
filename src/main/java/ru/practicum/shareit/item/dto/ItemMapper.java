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

    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getLastBooking(),
                item.getNextBooking(),
                item.getComments()
        );
    }

    public static ItemDtoResponse toDtoResponse(Item item) {
        return ItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}