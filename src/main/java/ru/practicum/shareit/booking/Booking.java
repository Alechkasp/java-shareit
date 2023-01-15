package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class Booking {
    private int id;

    @NotBlank
    private LocalDateTime start;

    @NotBlank
    private LocalDateTime end;

    private Item item;
    private User booker;
    private Status status;
}
