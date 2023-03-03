package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Variables.HEADER;

@Slf4j
@RequiredArgsConstructor
@Controller
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@PathVariable Long bookingId,
                                          @RequestHeader(HEADER) Long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(HEADER) Long ownerId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /bookings/owner?state={}&from={}&size={}.", state, from, size);
        return bookingClient.getAllByOwner(ownerId, state, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestHeader(HEADER) Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /bookings?state={}&from={}&size={}.", state, from, size);
        return bookingClient.getAllByBooker(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HEADER) Long userId,
                                         @Valid @RequestBody CreateBookingDto dto) {
        log.info("Получен запрос POST /bookings");
        return bookingClient.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER) Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam boolean approved) {
        log.info("Получен запрос PATCH /bookings/{}.", bookingId);
        return bookingClient.update(userId, bookingId, approved);
    }
}
