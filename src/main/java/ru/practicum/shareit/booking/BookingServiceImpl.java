package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.*;
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
                    return bookingRepository.findAllByItemOwnerId(userId,
                            Sort.by(Sort.Direction.DESC, "start"));
                case CURRENT:
                    return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(userId,
                            LocalDateTime.now(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                case PAST:
                    return bookingRepository.findAllByItemOwnerIdAndEndBefore(userId, LocalDateTime.now(),
                            Sort.by(Sort.Direction.DESC, "start"));
                case FUTURE:
                    return bookingRepository.findAllByItemOwnerIdAndStartAfter(userId, LocalDateTime.now(),
                            Sort.by(Sort.Direction.DESC, "start"));
                case WAITING:
                    return bookingRepository.findAllByItemOwnerIdAndStatus(userId, Status.WAITING,
                            Sort.by(Sort.Direction.DESC, "start"));
                case REJECTED:
                    return bookingRepository.findAllByItemOwnerIdAndStatus(userId, Status.REJECTED,
                            Sort.by(Sort.Direction.DESC, "start"));
                default:
                    throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new UserNotFoundException("Такого пользователя нет");
        }
    }

    @Transactional(readOnly = true)
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
                    return bookingRepository.findAllByBookerId(userId,
                            Sort.by(Sort.Direction.DESC, "start"));
                case CURRENT:
                    return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                            LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                case PAST:
                    return bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(),
                            Sort.by(Sort.Direction.DESC, "start"));
                case FUTURE:
                    return bookingRepository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now(),
                            Sort.by(Sort.Direction.DESC, "start"));
                case WAITING:
                    return bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING,
                            Sort.by(Sort.Direction.DESC, "start"));
                case REJECTED:
                    return bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED,
                            Sort.by(Sort.Direction.DESC, "start"));
                default:
                    throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new UserNotFoundException("Такого пользователя нет!");
        }
    }

    @Transactional
    @Override
    public Booking create(BookingDto bookingDto, Long userId, Long itemId) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        setUserAndItemForBooking(booking, userId, itemId);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return booking;
    }

    @Transactional
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

        return booking;
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
}
