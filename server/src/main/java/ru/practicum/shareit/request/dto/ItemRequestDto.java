package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ItemRequestDto {
    private Long id;

    private String description;

    private Long requesterId;

    private LocalDateTime created;

    private List<UpdateItemDto> items;
}
