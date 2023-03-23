package ru.practicum.main.event.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.main.event.model.dto.state.UpdateStatus;

import java.util.List;

@Getter
@Setter
@ToString
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    UpdateStatus status;
}
