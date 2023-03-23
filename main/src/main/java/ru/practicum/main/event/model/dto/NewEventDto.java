package ru.practicum.main.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.event.location.model.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class NewEventDto {
    @NotNull
    Long category;
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean paid = false;
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer participantLimit = 0;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean requestModeration = false;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
