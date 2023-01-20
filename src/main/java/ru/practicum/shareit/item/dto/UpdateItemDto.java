package ru.practicum.shareit.item.dto;

import lombok.Value;

@Value
public class UpdateItemDto {
    Integer id;
    String name;
    String description;
    Boolean available;
}
