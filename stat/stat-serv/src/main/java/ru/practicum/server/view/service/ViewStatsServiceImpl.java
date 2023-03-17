package ru.practicum.server.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.view.ViewStatsDto;
import ru.practicum.server.view.model.ViewStats;
import ru.practicum.server.view.model.ViewStatsMapper;
import ru.practicum.server.view.storage.ViewStatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewStatsServiceImpl implements ViewStatsService {

    private final ViewStatsRepository viewStatsRepository;

    private final ViewStatsMapper viewStatsMapper;

    @Override
    public List<ViewStatsDto> getViewStatsDto(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                              Optional<List<String>> uris, Boolean uniqueIp) {
        if (uniqueIp) {
            return getViewStatsDtoUniqueIp(startDateTime, endDateTime, uris);
        } else {
            return getViewStatsDtoNotUniqueIp(startDateTime, endDateTime, uris);
        }
    }

    private List<ViewStatsDto> getViewStatsDtoUniqueIp(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                       Optional<List<String>> uris) {
        List<ViewStats> viewStats;
        if (uris.isPresent()) {
            viewStats = viewStatsRepository.getViewStatsByDatesAndUriUniqueIp(startDateTime, endDateTime, uris.get());
        } else {
            viewStats = viewStatsRepository.getViewStatsByDatesUniqueIp(startDateTime, endDateTime);
        }
        return viewStats.stream()
                .map(viewStatsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

    private List<ViewStatsDto> getViewStatsDtoNotUniqueIp(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                          Optional<List<String>> uris) {
        List<ViewStats> viewStats;
        if (uris.isPresent()) {
            viewStats = viewStatsRepository.getViewStatsByDatesAndUri(startDateTime, endDateTime, uris.get());
        } else {
            viewStats = viewStatsRepository.getViewStatsByDates(startDateTime, endDateTime);
        }
        return viewStats.stream()
                .map(viewStatsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

}
