package ru.practicum.main.compilation.model.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@ToString
public class NewCompilationDto {
    private Set<Long> events;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean pinned = false;
    @NotBlank
    private String title;
}
