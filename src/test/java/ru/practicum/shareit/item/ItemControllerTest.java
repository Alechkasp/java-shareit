package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private ItemDtoResponse itemDtoResponse;
    private CreateItemDto createItemDto;
    private UpdateItemDto updateItemDto;
    private CreateCommentDto createCommentDto;
    private CommentDto commentDto;

    @BeforeEach
    void create() {
        itemDto = new ItemDto(1L, "name", "description", true, null,
                null, null, 1L);

        itemDtoResponse = new ItemDtoResponse(1L, "name", "description", true, null,
                null, null);

        createItemDto = new CreateItemDto(1L, "name", "description", true, 1L);

        updateItemDto = new UpdateItemDto(1L, "name", "description", true, 1L);

        createCommentDto = new CreateCommentDto("comment");

        commentDto = new CommentDto(1L, "comment", "Alex", LocalDateTime.now());
    }

    @SneakyThrows
    @Test
    void getByUserId() {
        Integer from = 0;
        Integer size = 20;
        Long userId = 1L;

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(itemService).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getByUserId_whenRequestParamIsDefault() {
        long userId = 1;
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(itemService).getByUserId(userId, 0, 10);
    }

    @SneakyThrows
    @Test
    void getById() {
        Long itemId = 1L;
        Long userId = 1L;
        Mockito.when(itemService.getById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemDto);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
        Mockito.verify(itemService).getById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void create_shouldReturnOk() {
        Mockito.when(itemService.create(Mockito.anyLong(), Mockito.any())).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void create_whenItemIsNotValid_thenReturnedBadRequest() {
        ItemDto itemDtoIsNotValid = new ItemDto(1L, "", "description", true,
                null, null, null, 1L);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoIsNotValid)))
                .andExpect(status().isBadRequest());
        Mockito.verify(itemService, Mockito.never()).create(Mockito.any(), Mockito.any());

        itemDtoIsNotValid = new ItemDto(1L, "name", "description", true,
                null, null, null, -1L);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoIsNotValid)))
                .andExpect(status().isBadRequest());
        Mockito.verify(itemService, Mockito.never()).create(Mockito.any(), Mockito.any());

        itemDtoIsNotValid = new ItemDto(1L, "name", "description", null,
                null, null, null, -1L);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoIsNotValid)))
                .andExpect(status().isBadRequest());
        Mockito.verify(itemService, Mockito.never()).create(Mockito.any(), Mockito.any());

        itemDtoIsNotValid = new ItemDto(1L, "name", "", true,
                null, null, null, -1L);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoIsNotValid)))
                .andExpect(status().isBadRequest());
        Mockito.verify(itemService, Mockito.never()).create(Mockito.any(), Mockito.any());
    }

    @SneakyThrows
    @Test
    void update() {
        Long itemId = 1L;

        Mockito.when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void search_whenTextIsNotEmpty() {
        Integer from = 0;
        Integer size = 20;
        String text = "text";

        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(itemService).search(text, from, size);
    }

    @SneakyThrows
    @Test
    void search_whenTextIsEmpty() {
        Integer from = 0;
        Integer size = 20;
        String text = "";

        String result = mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of()), result);
    }

    @SneakyThrows
    @Test
    void createComment() {
        Long id = 1L;

        Mockito.when(itemService.createComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{id}/comment", id)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}