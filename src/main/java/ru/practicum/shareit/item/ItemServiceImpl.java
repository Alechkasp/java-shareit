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

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<Item> getByUserId(Long userId) {

        List<Item> itemList = itemRepository.findAllByOwnerId(userId);

        boolean isOwner = true;

        List<Booking> bookingsList = new ArrayList<>();

        for (Item item : itemList) {
            if (!item.getOwner().getId().equals(userId)) {
                isOwner = false;
            }
            bookingsList.addAll(bookingRepository.findAllByItemId(item.getId(),
                    Sort.by(Sort.Direction.DESC, "start")));
        }

        if (!isOwner) {
            throw new UserNotFoundException("Пользователь не является владельцем вещей");
        }

        for (Booking b : bookingsList) {
            for (Item item : itemList) {
                if (b.getItem().getId().equals(item.getId())) {
                    item.setNextBooking(BookingMapper.toDtoShort(bookingsList.get(0)));
                    item.setLastBooking(BookingMapper.toDtoShort(bookingsList.get(1)));
                }
            }
        }
        return itemList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text.toLowerCase());
    }

    @Transactional(readOnly = true)
    @Override
    public Item getById(Long itemId, Long userId) {
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
                return item;
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

        return item;
    }

    @Transactional
    @Override
    public Item create(Long userId, CreateItemDto itemDto) {
        Item item = ItemMapper.createItemDtoToItem(itemDto);

        item.setOwner(userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Такого пользователя нет!")));

        itemRepository.save(item);
        return item;
    }

    @Transactional
    @Override
    public Item update(Long itemId, Long userId, UpdateItemDto itemDto) {
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

        return item;
    }

    @Transactional
    @Override
    public Item delete(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Такой вещи нет!"));
        itemRepository.deleteById(itemId);
        return item;
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
}
