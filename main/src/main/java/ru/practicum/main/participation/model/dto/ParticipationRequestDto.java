package ru.practicum.main.participation.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.participation.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status = Status.PENDING;


}
