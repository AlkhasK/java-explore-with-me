package ru.practicum.client.hit;

import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.client.base.BaseClient;
import ru.practicum.dto.hit.HitCreateDto;

public class HitClient extends BaseClient {

    private static final String API_PREFIX = "/hit";

    public HitClient(String baseUrl) {
        super(WebClient.builder()
                .baseUrl(baseUrl)
                .build()
        );
    }

    public void post(HitCreateDto hitCreateDto) {
        post(API_PREFIX, hitCreateDto);
    }
}
