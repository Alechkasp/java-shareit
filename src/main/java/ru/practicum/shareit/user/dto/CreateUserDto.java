package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class CreateUserDto {
    Integer id;

    @NotBlank
    String name;

    @Email
    @NotBlank
    String email;
}
