package ru.practicum.server.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.view.ViewStatsDto;
import ru.practicum.server.hit.model.Hit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
@Transactional
public class ViewTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private Hit hit;

    @BeforeEach
    public void initHit() {
        hit = new Hit();
        hit.setApp("ewm-main-service");
        hit.setUri("/events/1");
        hit.setIp("200.250.10.181");
        hit.setTimestamp(LocalDateTime.now().minusMinutes(1).truncatedTo(ChronoUnit.SECONDS));
    }

    @SneakyThrows
    @Test
    public void createHitTest() {
        int expectedViewStatsDtoAmount = 1;
        int expectedHits = 1;

        Hit dbHit = testEntityManager.persistAndFlush(hit);

        String result = mockMvc.perform(get("/stats")
                        .accept("application/json")
                        .param("start", "2020-05-05 00:00:00")
                        .param("end", "2035-05-05 00:00:00")
                        .param("uris", "/events/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ViewStatsDto> viewStatsDtos = Arrays.asList(objectMapper.readValue(result, ViewStatsDto[].class));
        assertThat(viewStatsDtos).hasSize(expectedViewStatsDtoAmount);
        ViewStatsDto viewStatsDto = viewStatsDtos.get(0);
        assertThat(viewStatsDto.getApp()).isEqualTo(dbHit.getApp());
        assertThat(viewStatsDto.getUri()).isEqualTo(dbHit.getUri());
        assertThat(viewStatsDto.getHits()).isEqualTo(expectedHits);
    }
}
