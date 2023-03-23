package ru.practicum.main.user.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class NewUserRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;

}
