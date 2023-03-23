package ru.practicum.main.compilation.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.event.model.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
@ToString
public class CompilationDto {
    private Long id;
    private Set<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
