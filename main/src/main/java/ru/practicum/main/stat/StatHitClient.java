package ru.practicum.main.stat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.client.hit.HitClient;
import ru.practicum.dto.hit.HitCreateDto;

import java.time.LocalDateTime;

@Slf4j
@Component
public class StatHitClient {

    private static final String appName = "ewm-service";

    private final HitClient hitClient;

    public StatHitClient(@Value("${stat-server.url}") String statServerUrl) {
        hitClient = new HitClient(statServerUrl);
    }

    public void registerHit(String ip, String path) {
        HitCreateDto hit = new HitCreateDto();
        hit.setIp(ip);
        hit.setUri(path);
        hit.setApp(appName);
        hit.setTimestamp(LocalDateTime.now());
        log.info("Register hit : {}", hit);
        hitClient.post(hit);
    }
}
