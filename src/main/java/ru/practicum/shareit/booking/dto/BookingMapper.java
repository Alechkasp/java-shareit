package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static Booking toBooking(BookingDto dto) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .build();
    }

    public static BookingDtoShort toDtoShort(Booking booking) {
        return BookingDtoShort.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

/*    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .item(booking.getItem())
                .build();
    }

    public static BookingDtoShort toDtoShort(Booking booking) {
        return BookingDtoShort.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static Booking toBookingFromDto(BookingDto dto) {
        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .booker(dto.getBooker())
                .status(dto.getStatus())
                .item(dto.getItem())
                .build();
    }

    public static Booking toBooking(BookingDtoShort dto) {
        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .build();
    }*/

/*    public static BookingDto toDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setItem(booking.getItem());
        return bookingDto;
    }

    public static BookingDtoShort toDtoShort(Booking booking) {
        BookingDtoShort bookingDtoShort = new BookingDtoShort();
        bookingDtoShort.setId(booking.getId());
        bookingDtoShort.setStart(booking.getStart());
        bookingDtoShort.setEnd(booking.getEnd());
        bookingDtoShort.setItemId(booking.getItem().getId());
        bookingDtoShort.setBookerId(booking.getBooker().getId());
        return bookingDtoShort;
    }

    public static Booking toBookingFromBookingDto(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(bookingDto.getStatus());
        booking.setBooker(bookingDto.getBooker());
        booking.setItem(bookingDto.getItem());
        return booking;
    }

    public static Booking toBookingFromBookingDtoShort(BookingDtoShort bookingDtoShort) {
        Booking booking = new Booking();
        booking.setId(bookingDtoShort.getId());
        booking.setStart(bookingDtoShort.getStart());
        booking.setEnd(bookingDtoShort.getEnd());
        return booking;
    }*/
}
