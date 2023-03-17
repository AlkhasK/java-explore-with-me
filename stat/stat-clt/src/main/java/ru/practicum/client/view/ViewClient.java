package ru.practicum.client.view;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.annotation.Nullable;
import ru.practicum.client.base.BaseClient;
import ru.practicum.dto.view.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewClient extends BaseClient {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String API_PREFIX = "/stats";

    public ViewClient(String baseUrl) {
        super(WebClient.builder()
                .baseUrl(baseUrl)
                .build()
        );
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, @Nullable List<String> uris, @Nullable Boolean uniqueIp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("start", start.format(formatter));
        params.add("end", end.format(formatter));
        if (uris != null) {
            params.add("uris", String.join(",", uris));
        }
        if (uniqueIp != null) {
            params.add("unique", uniqueIp.toString());
        }
        return get(API_PREFIX, params);
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris) {
        return get(start, end, uris, null);
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end) {
        return get(start, end, null, null);
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, Boolean uniqueIp) {
        return get(start, end, null, uniqueIp);
    }
}
