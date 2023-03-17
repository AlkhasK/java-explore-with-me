package ru.practicum.server.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.view.ViewStatsDto;
import ru.practicum.server.view.service.ViewStatsService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ViewStatsController.class)
public class ViewStatsControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ViewStatsService viewStatsService;

    @Autowired
    private MockMvc mockMvc;

    private ViewStatsDto viewStatsDto;

    @BeforeEach
    public void initViewStatsDto() {
        viewStatsDto = new ViewStatsDto();
        viewStatsDto.setApp("ewm-main-service");
        viewStatsDto.setUri("/events/1");
        viewStatsDto.setHits(1L);
    }

    @SneakyThrows
    @Test
    public void findWhenHitCreateDtoValid_ThenReturnViewStatsDto() {
        Mockito.when(viewStatsService.getViewStatsDto(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(viewStatsDto));

        String result = mockMvc.perform(get("/stats")
                        .accept("application/json")
                        .param("start", "2020-05-05 00:00:00")
                        .param("end", "2035-05-05 00:00:00")
                        .param("uris", "/events/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(List.of(viewStatsDto)));
    }
}
