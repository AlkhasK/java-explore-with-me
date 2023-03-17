package ru.practicum.server.hit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.hit.HitCreateDto;
import ru.practicum.server.hit.service.HitService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HitController.class)
public class HitControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HitService hitService;

    @Autowired
    private MockMvc mockMvc;

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
    public void createWhenHitCreateDtoValid_ThenReturnOk() {
        mockMvc.perform(post("/hit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(hitCreateDto)))
                .andExpect(status().isOk());

        Mockito.verify(hitService, Mockito.times(1)).createHit(Mockito.any());
    }
}
