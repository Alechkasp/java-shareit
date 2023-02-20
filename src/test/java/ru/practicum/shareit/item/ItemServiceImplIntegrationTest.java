package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegrationTest {
    private final ItemServiceImpl itemService;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final ItemRequestRepository itemRequestRepository;

    private Long itemId;
    private Long userId;
    private Long requestId;
    private Long commentId;
    private User user;
    private Item item;
    private Comment comment;
    private Booking booking;
    private ItemRequest itemRequest;
    private ItemDtoResponse itemDtoResponse;
    private ItemDto itemDto;
    private CreateItemDto createItemDto;
    private UpdateItemDto updateItemDto;

    @BeforeEach
    void beforeEach() {
        user = new User(null, "Alex", "alex.b@yandex.ru");
        user = userRepository.save(user);
        userId = user.getId();

        item = new Item(null, "item bag", "description", true, user,
                null, null, null, null);
        item = itemRepository.save(item);
        itemId = item.getId();

        booking = new Booking(null, LocalDateTime.now(), LocalDateTime.now(), item, user, Status.APPROVED);
        booking = bookingRepository.save(booking);

        comment = new Comment(null, "comment", item, user, LocalDateTime.now());
        comment = commentRepository.save(comment);
        commentId = comment.getId();

        itemRequest = new ItemRequest(null, "description", user, LocalDateTime.now(),null);
        itemRequest = itemRequestRepository.save(itemRequest);
        requestId = itemRequest.getId();
    }

    @Test
    void getByUserId() {
        Integer from = 0;
        Integer size = 10;

        itemDtoResponse = ItemMapper.toDtoResponse(item);
        itemDtoResponse.setComments(List.of(CommentMapper.toDtoResponse(comment)));
        itemDtoResponse.setLastBooking(BookingMapper.toDtoShortResponse(booking));

        List<ItemDtoResponse> actualDtoList = itemService.getByUserId(userId, from, size);
        List<ItemDtoResponse> expectedDtoList = List.of(itemDtoResponse);

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void search() {
        Integer from = 0;
        Integer size = 10;
        String text = "desc";

        itemDto = ItemMapper.toDto(item);

        List<ItemDto> actualDtoList = itemService.search(text, from, size);
        List<ItemDto> expectedDtoList = List.of(itemDto);

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void getById() {
        itemDto = ItemMapper.toDto(item);
        itemDto.setComments(List.of(CommentMapper.toDto(comment)));
        itemDto.setLastBooking(BookingMapper.toDtoShort(booking));

        ItemDto expectedDto = itemDto;
        ItemDto actualDto = itemService.getById(itemId, userId);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void create() {
        createItemDto = new CreateItemDto(null, "new", "description",
                true, requestId);

        ItemDto saveItem = itemService.create(userId, createItemDto);

        assertEquals(itemId + 1, saveItem.getId());
        assertEquals("new", saveItem.getName());
        assertEquals("description", saveItem.getDescription());
        assertEquals(requestId, saveItem.getRequestId());
        assertEquals(true, saveItem.getAvailable());
    }

    @Test
    void delete() {
        Long itemId = 3L;
        createItemDto = new CreateItemDto(3L, "new", "description",
                true, requestId);

        itemService.delete(itemId);

        assertThatThrownBy(() -> itemService.getById(userId, itemId)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void createComment() {
        comment = new Comment(null, "comment", item, user, LocalDateTime.now());
        comment = commentRepository.save(comment);

        assertEquals(commentId + 1, comment.getId());
        assertEquals("comment", comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(user, comment.getAuthor());
    }
}
