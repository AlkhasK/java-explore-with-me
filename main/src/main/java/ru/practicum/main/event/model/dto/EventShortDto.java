package ru.practicum.main.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.category.model.dto.CategoryDto;
import ru.practicum.main.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
    @JsonIgnore
    private Integer participantLimit;
}
