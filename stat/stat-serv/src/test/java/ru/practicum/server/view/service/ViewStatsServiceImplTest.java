package ru.practicum.server.view.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.view.ViewStatsDto;
import ru.practicum.server.view.model.ViewStats;
import ru.practicum.server.view.model.ViewStatsMapper;
import ru.practicum.server.view.storage.ViewStatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ViewStatsServiceImplTest {

    @Mock
    private ViewStatsRepository viewStatsRepository;

    @Mock
    private ViewStatsMapper viewStatsMapper;

    @InjectMocks
    private ViewStatsServiceImpl viewStatsService;

    private final LocalDateTime start = LocalDateTime.now();

    private final LocalDateTime end = LocalDateTime.now();

    @Test
    public void getViewStatsDtoWhenExistUrisAndUniqueIp_ThenReturnViewStatsDto() {
        List<String> uris = List.of("uri");
        Optional<List<String>> urisOpt = Optional.of(uris);
        Boolean uniqueIp = true;
        ViewStats viewStats = new ViewStats("", "", 0L);
        ViewStatsDto viewStatsDto = new ViewStatsDto();

        Mockito.when(viewStatsRepository.getViewStatsByDatesAndUriUniqueIp(start, end, uris))
                .thenReturn(List.of(viewStats));
        Mockito.when(viewStatsMapper.toViewStatsDto(Mockito.any()))
                .thenReturn(viewStatsDto);

        viewStatsService.getViewStatsDto(start, end, urisOpt, uniqueIp);

        Mockito.verify(viewStatsRepository, Mockito.times(1))
                .getViewStatsByDatesAndUriUniqueIp(start, end, uris);
    }

    @Test
    public void getViewStatsDtoWhenExistUris_ThenReturnViewStatsDto() {
        List<String> uris = List.of("uri");
        Optional<List<String>> urisOpt = Optional.of(uris);
        Boolean uniqueIp = false;
        ViewStats viewStats = new ViewStats("", "", 0L);
        ViewStatsDto viewStatsDto = new ViewStatsDto();

        Mockito.when(viewStatsRepository.getViewStatsByDatesAndUri(start, end, uris))
                .thenReturn(List.of(viewStats));
        Mockito.when(viewStatsMapper.toViewStatsDto(Mockito.any()))
                .thenReturn(viewStatsDto);

        viewStatsService.getViewStatsDto(start, end, urisOpt, uniqueIp);

        Mockito.verify(viewStatsRepository, Mockito.times(1))
                .getViewStatsByDatesAndUri(start, end, uris);
    }

    @Test
    public void getViewStatsDtoWhenUniqueIp_ThenReturnViewStatsDto() {
        Optional<List<String>> urisOpt = Optional.empty();
        Boolean uniqueIp = true;
        ViewStats viewStats = new ViewStats("", "", 0L);
        ViewStatsDto viewStatsDto = new ViewStatsDto();

        Mockito.when(viewStatsRepository.getViewStatsByDatesUniqueIp(start, end))
                .thenReturn(List.of(viewStats));
        Mockito.when(viewStatsMapper.toViewStatsDto(Mockito.any()))
                .thenReturn(viewStatsDto);

        viewStatsService.getViewStatsDto(start, end, urisOpt, uniqueIp);

        Mockito.verify(viewStatsRepository, Mockito.times(1))
                .getViewStatsByDatesUniqueIp(start, end);
    }

    @Test
    public void getViewStatsDto_ThenReturnViewStatsDto() {
        Optional<List<String>> urisOpt = Optional.empty();
        Boolean uniqueIp = false;
        ViewStats viewStats = new ViewStats("", "", 0L);
        ViewStatsDto viewStatsDto = new ViewStatsDto();

        Mockito.when(viewStatsRepository.getViewStatsByDates(start, end))
                .thenReturn(List.of(viewStats));
        Mockito.when(viewStatsMapper.toViewStatsDto(Mockito.any()))
                .thenReturn(viewStatsDto);

        viewStatsService.getViewStatsDto(start, end, urisOpt, uniqueIp);

        Mockito.verify(viewStatsRepository, Mockito.times(1))
                .getViewStatsByDates(start, end);
    }
}
