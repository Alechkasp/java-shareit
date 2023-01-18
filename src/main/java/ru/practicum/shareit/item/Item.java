package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    Integer id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    Boolean available;
    User owner;
    ItemRequest request;
}
