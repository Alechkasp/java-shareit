package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking getById(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new BookingNotFoundException("Такого бронирования нет!"));

        if (booking.getBooker().getId().equals(userId)
                || booking.getItem().getOwner().getId().equals(userId)) {
            return booking;
        } else {
            throw new UserNotFoundException("Пользователь не является арендатором или владельцем вещи!");
        }
    }

    @Override
    public List<Booking> getAllByOwnerId(Long userId, String state) {
        if (userRepository.existsById(userId)) {
            try {
                State.valueOf(state);
            } catch (RuntimeException e) {
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                case CURRENT:
                    return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now());
                case PAST:
                    return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                case WAITING:
                    return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                case REJECTED:
                    return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                default:
                    throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new UserNotFoundException("Такого пользователя нет");
        }
    }

    @Override
    public List<Booking> getAllByBookerId(Long userId, String state) {
        if (userRepository.existsById(userId)) {
            try {
                State.valueOf(state);
            } catch (RuntimeException e) {
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                case CURRENT:
                    return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now());
                case PAST:
                    return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                case WAITING:
                    return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                case REJECTED:
                    return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                default:
                    throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new UserNotFoundException("Такого пользователя нет!");
        }
    }

    @Override
    public Booking create(BookingDto bookingDto, Long userId, Long itemId) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        validate(booking);
        setUserAndItemForBooking(booking, userId, itemId);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking update(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Такого бронирования нет!"));

        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new UnavailableException("Бронирование уже подтверждено!");
        }

        if (!itemRepository.findById(booking.getItem().getId()).orElseThrow().getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("Пользователь не является владельцем вещи");
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingRepository.save(booking);
    }

    private void setUserAndItemForBooking(Booking booking, Long userId, Long itemId) {
        booking.setBooker(userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!")));

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна");
        }

        if (userId.equals(item.getOwner().getId())) {
            throw new ItemNotFoundException("Вещь не может быть забронировать ее владелец");
        }

        booking.setItem(item);
    }

    private void validate(Booking booking) {
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Забронировать в прошлом нельзя");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Завершение брони раньше ее регистрации");
        }
    }
}
