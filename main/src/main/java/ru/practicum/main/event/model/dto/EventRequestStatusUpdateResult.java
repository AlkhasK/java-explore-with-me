package ru.practicum.main.event.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.participation.model.dto.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@ToString
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
