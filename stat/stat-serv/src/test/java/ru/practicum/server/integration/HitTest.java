package ru.practicum.server.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.hit.HitCreateDto;
import ru.practicum.server.hit.model.Hit;
import ru.practicum.server.hit.storage.HitRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
@Transactional
public class HitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HitRepository hitRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private HitCreateDto hitCreateDto;

    @BeforeEach
    public void initHitCreateDto() {
        hitCreateDto = new HitCreateDto();
        hitCreateDto.setApp("ewm-main-service");
        hitCreateDto.setUri("/events/1");
        hitCreateDto.setIp("200.250.10.181");
        hitCreateDto.setTimestamp(LocalDateTime.now().minusMinutes(1).truncatedTo(ChronoUnit.SECONDS));
    }

    @SneakyThrows
    @Test
    public void createHitTest() {
        int expectedHitAmount = 1;

        mockMvc.perform(post("/hit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(hitCreateDto)))
                .andExpect(status().isOk());

        List<Hit> hits = hitRepository.findAll();
        assertThat(hits).hasSize(expectedHitAmount);
        Hit hit = hits.get(0);
        assertThat(hit.getApp()).isEqualTo(hitCreateDto.getApp());
        assertThat(hit.getUri()).isEqualTo(hitCreateDto.getUri());
        assertThat(hit.getIp()).isEqualTo(hitCreateDto.getIp());
        assertThat(hit.getTimestamp()).isEqualTo(hitCreateDto.getTimestamp());
    }
}
