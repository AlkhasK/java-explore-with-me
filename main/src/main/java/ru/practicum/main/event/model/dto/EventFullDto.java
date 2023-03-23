package ru.practicum.main.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.category.model.dto.CategoryDto;
import ru.practicum.main.event.location.model.dto.LocationDto;
import ru.practicum.main.event.model.State;
import ru.practicum.main.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class EventFullDto {
    private Long views;
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit = 0;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration = true;
    private State state;
    private String title;
}