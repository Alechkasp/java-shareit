package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    Integer id;
    String name;
    String description;
    Boolean available;
    User owner;
    Integer request;
}
