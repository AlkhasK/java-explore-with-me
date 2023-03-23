package ru.practicum.main.event.helper.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AdminSearchEventsParameters {
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
