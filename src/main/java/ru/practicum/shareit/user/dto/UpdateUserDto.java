package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;

@Value
public class UpdateUserDto {
    Integer id;

    String name;

    @Email
    String email;
}
