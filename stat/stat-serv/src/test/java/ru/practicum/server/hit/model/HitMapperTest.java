package ru.practicum.server.hit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.hit.HitCreateDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class HitMapperTest {

    private final HitMapper hitMapper = new HitMapper();

    @Test
    public void toHitWhenHitCreateDtoValid_ThenReturnHit() {
        String app = "ewm-main-service";
        String uri = "/events/1";
        String ip = "200.250.10.181";
        LocalDateTime timestamp = LocalDateTime.now().minusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
        HitCreateDto hitCreateDto = new HitCreateDto();
        hitCreateDto.setApp(app);
        hitCreateDto.setUri(uri);
        hitCreateDto.setIp(ip);
        hitCreateDto.setTimestamp(timestamp);

        Hit result = hitMapper.toHit(hitCreateDto);

        assertThat(result.getApp()).isEqualTo(app);
        assertThat(result.getUri()).isEqualTo(uri);
        assertThat(result.getIp()).isEqualTo(ip);
        assertThat(result.getTimestamp()).isEqualTo(timestamp);
    }
}
