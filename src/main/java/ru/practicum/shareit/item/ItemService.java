package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;

import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {

    List<Item> getByUserId(Long userId);

    List<Item> search(String text);

    Item getById(Long itemId, Long userId);

    Item create(Long userId, CreateItemDto itemDto);

    Item update(Long itemId, Long userId, UpdateItemDto itemDto);

    Item delete(Long itemId);

    CommentDto createComment(Long itemId, Long userId, CreateCommentDto commentDto);
}
