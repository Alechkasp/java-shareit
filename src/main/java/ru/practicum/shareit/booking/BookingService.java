package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking create(BookingDto bookingDto, Long userId, Long itemId);

    Booking update(Long userId, Long bookingId, boolean approved);

    Booking getById(Long id, Long userId);

    List<Booking> getAllByBookerId(Long userId, String state);

    List<Booking> getAllByOwnerId(Long userId, String state);
}
