package ru.practicum.server.view.model;

import org.springframework.stereotype.Component;
import ru.practicum.dto.view.ViewStatsDto;

@Component
public class ViewStatsMapper {

    public ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        ViewStatsDto viewStatsDto = new ViewStatsDto();
        viewStatsDto.setApp(viewStats.getApp());
        viewStatsDto.setUri(viewStats.getUri());
        viewStatsDto.setHits(viewStats.getHits());
        return viewStatsDto;
    }
}
