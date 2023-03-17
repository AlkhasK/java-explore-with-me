package ru.practicum.server.view.model;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.view.ViewStatsDto;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewStatsMapperTest {

    private final ViewStatsMapper viewStatsMapper = new ViewStatsMapper();

    @Test
    public void toViewStatsDtoWhenViewStatsValid_ThenReturnViewStatsDto() {
        String app = "ewm-main-service";
        String uri = "/events/1";
        Long hits = 0L;
        ViewStats viewStats = new ViewStats(app, uri, hits);

        ViewStatsDto result = viewStatsMapper.toViewStatsDto(viewStats);

        assertThat(result.getApp()).isEqualTo(app);
        assertThat(result.getUri()).isEqualTo(uri);
        assertThat(result.getHits()).isEqualTo(hits);
    }
}
