package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ItemDto {
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    @Valid
    private Boolean available;
}
