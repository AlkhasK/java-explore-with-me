package ru.practicum.main.stat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.client.view.ViewClient;
import ru.practicum.dto.view.ViewStatsDto;
import ru.practicum.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatViewClient {

    private final static int DELTA_YEARS_FOR_INTERVAL = 1;

    private final ViewClient viewClient;

    public StatViewClient(@Value("${stat-server.url}") String statServerUrl) {
        viewClient = new ViewClient(statServerUrl);
    }

    public List<ViewStatsDto> getViews(List<String> uri, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return viewClient.get(rangeStart, rangeEnd, uri);
    }

    public Map<Long, Long> getHits(List<Event> events, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().minusYears(DELTA_YEARS_FOR_INTERVAL);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(DELTA_YEARS_FOR_INTERVAL);
        }
        rangeStart = rangeStart.minusYears(DELTA_YEARS_FOR_INTERVAL);
        rangeEnd = rangeEnd.plusYears(DELTA_YEARS_FOR_INTERVAL);
        List<String> eventsUri = events.stream()
                .map(ent -> getEventUri(ent.getId()))
                .collect(Collectors.toList());
        List<ViewStatsDto> views = getViews(eventsUri, rangeStart, rangeEnd);
        return views.stream()
                .collect(Collectors.toMap(view -> getEventId(view.getUri()),
                        ViewStatsDto::getHits,
                        (existing, replacement) -> existing));
    }

    public Long getHit(Long eventId) {
        List<ViewStatsDto> views = getViews(List.of(getEventUri(eventId)),
                LocalDateTime.now().minusYears(DELTA_YEARS_FOR_INTERVAL),
                LocalDateTime.now().plusYears(DELTA_YEARS_FOR_INTERVAL));
        if (views.size() == 0) {
            return 0L;
        } else {
            return views.get(0).getHits();
        }
    }

    private String getEventUri(Long eventId) {
        String uriPart = "/events/";
        return uriPart.concat(eventId.toString());
    }

    private Long getEventId(String uri) {
        String[] splitUri = uri.split("/");
        String eventId = splitUri[splitUri.length - 1];
        return Long.valueOf(eventId);
    }
}
