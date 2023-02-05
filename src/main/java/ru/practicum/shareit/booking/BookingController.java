package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private static final String HEADER = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public Booking getById(@PathVariable Long bookingId,
                           @RequestHeader(name = HEADER) Long bookerId) {
        log.info("Получен запрос GET /bookings/{bookingId}. " + bookingId);
        return bookingService.getById(bookingId, bookerId);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner(@RequestHeader(name = HEADER) Long ownerId,
                                       @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос GET /bookings/owner?state={state}. " + state);
        return bookingService.getAllByOwnerId(ownerId, state);
    }

    @GetMapping
    public List<Booking> getAllByBooker(@RequestHeader(name = HEADER) Long bookerId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос GET /bookings?state={state}. " + state);
        return bookingService.getAllByBookerId(bookerId, state);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@RequestHeader(name = HEADER) Long userId,
                          @Valid @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос POST /bookings");
        return bookingService.create(bookingDto, userId, bookingDto.getItemId());
    }

    @PatchMapping("/{bookingId}")
    public Booking update(@RequestHeader(name = HEADER) Long userId,
                          @PathVariable Long bookingId,
                          @RequestParam boolean approved) {
        log.info("Получен запрос PATCH /bookings/{bookingId}. " + bookingId);
        return bookingService.update(userId, bookingId, approved);
    }
}
