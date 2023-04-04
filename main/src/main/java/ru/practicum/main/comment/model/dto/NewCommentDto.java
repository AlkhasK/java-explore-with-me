package ru.practicum.main.comment.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class NewCommentDto {
    @NotBlank
    private String text;
}
