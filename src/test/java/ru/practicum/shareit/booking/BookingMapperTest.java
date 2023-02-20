package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookingMapperTest {

    @Test
    void bookingToShortBookingDto() {
        User user = new User(1L, "Alex", "alex.b@yandex.ru");
        Item item = new Item(1L, "bag", "description", true, user,
                null, null, null, null);
        Booking booking = new Booking(1L, null, null, item, user, Status.WAITING);

        BookingDtoShort dto = BookingMapper.toDtoShort(booking);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getBookerId()).isEqualTo(1L);
    }
}