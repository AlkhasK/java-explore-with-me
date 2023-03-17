package ru.practicum.server.view.service;

import ru.practicum.dto.view.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ViewStatsService {

    List<ViewStatsDto> getViewStatsDto(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                       Optional<List<String>> uris, Boolean uniqueIp);

}
