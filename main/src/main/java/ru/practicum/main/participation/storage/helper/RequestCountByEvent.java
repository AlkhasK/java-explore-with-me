package ru.practicum.main.participation.storage.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestCountByEvent {
    private Long eventId;
    private Long count;
}
