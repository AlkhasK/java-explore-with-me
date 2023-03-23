package ru.practicum.main.user.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String email;
    private String name;
}
