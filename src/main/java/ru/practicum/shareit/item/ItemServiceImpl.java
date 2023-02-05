package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.comment.CreateCommentDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.CreateItemDto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<ItemDto> getByUserId(Long userId) {

        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(item -> {
                    List<Booking> bookings = bookingRepository.findAllByItemId(item.getId(),
                            Sort.by(Sort.Direction.DESC, "start"));
                    Booking nextBooking = getNextBooking(bookings);
                    Booking lastBooking = getLastBooking(bookings);
                    item.setNextBooking(nextBooking != null ? BookingMapper.toDtoShort(nextBooking) : null);
                    item.setLastBooking(lastBooking != null ? BookingMapper.toDtoShort(lastBooking) : null);
                    item.setComments(
                            commentRepository.findByItemId(item.getId())
                                    .stream()
                                    .map(CommentMapper::toDto)
                                    .collect(Collectors.toList())
                    );

                    return ItemMapper.toDto(item);
                })
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text.toLowerCase()).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));

        item.setComments(commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList()));

        if (item.getOwner().getId().equals(userId)) {

            List<Booking> bookings = bookingRepository.findAllByItemId(itemId,
                    Sort.by(Sort.Direction.DESC, "start"));

            if (bookings.isEmpty()) {
                return ItemMapper.toDto(item);
            }

            if (bookings.size() > 1) {
                item.setNextBooking(BookingMapper.toDtoShort(bookings.get(0)));
                item.setLastBooking(BookingMapper.toDtoShort(bookings.get(1)));
            }

            if (bookings.size() == 1) {
                item.setNextBooking(BookingMapper.toDtoShort(bookings.get(0)));
                item.setLastBooking(BookingMapper.toDtoShort(bookings.get(0)));
            }
        }

        return ItemMapper.toDto(item);
    }

    @Transactional
    @Override
    public ItemDto create(Long userId, CreateItemDto itemDto) {
        Item item = ItemMapper.createItemDtoToItem(itemDto);

        item.setOwner(userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!")));

        itemRepository.save(item);
        return ItemMapper.toDto(item);
    }

    @Transactional
    @Override
    public ItemDto update(Long itemId, Long userId, UpdateItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!"));

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));

        if (!user.equals(item.getOwner())) {
            throw new UserNotFoundException("Пользователь не является владельцем вещи!");
        }

        if ((itemDto.getName() != null) && (!itemDto.getName().isBlank())) {
            item.setName(itemDto.getName());
        }

        if ((itemDto.getDescription() != null) && (!itemDto.getDescription().isBlank())) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toDto(item);
    }

    @Transactional
    @Override
    public ItemDto delete(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));
        itemRepository.deleteById(itemId);
        return ItemMapper.toDto(item);
    }

    @Transactional
    @Override
    public CommentDto createComment(Long itemId, Long userId, CreateCommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет"));
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет"));

        if (!bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())
                .isEmpty()) {
            Comment comment = CommentMapper.toComment(commentDto);
            comment.setItem(item);
            comment.setAuthor(user);
            comment.setCreated(LocalDateTime.now());

            return CommentMapper.toDto(commentRepository.save(comment));
        } else {
            throw new UnavailableException("Невозможно оставить комментарий");
        }
    }

    private Booking getNextBooking(List<Booking> bookings) {
        List<Booking> filteredBookings = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        return filteredBookings.isEmpty() ? null : filteredBookings.get(0);
    }

    private Booking getLastBooking(List<Booking> bookings) {
        List<Booking> filteredBookings = bookings.stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        return filteredBookings.isEmpty() ? null : filteredBookings.get(filteredBookings.size() - 1);
    }
}
