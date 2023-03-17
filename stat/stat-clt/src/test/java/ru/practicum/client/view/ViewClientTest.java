package ru.practicum.client.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.view.ViewStatsDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ViewClientTest {

    private static MockWebServer mockBackEnd;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ViewClient.DATE_PATTERN);
    private ViewClient viewClient;

    @BeforeAll
    public static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        viewClient = new ViewClient(baseUrl);
    }

    @SneakyThrows
    @Test
    public void getWhenDatesUrisUniqueIp_ThenReturnListViewStatsDto() {
        String expectedMethod = "GET";
        int expectedReturnSize = 1;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = List.of("/events/1", "/events/2");
        Boolean unique = true;
        String expectedPath = String.format("/stats?start=%s&end=%s&uris=%s&unique=%s",
                start.format(formatter),
                end.format(formatter),
                String.join(",", uris),
                true);
        expectedPath = UriComponentsBuilder
                .fromUriString(expectedPath)
                .build()
                .encode()
                .toUri()
                .toString();
        ViewStatsDto mockViewStatsDto = new ViewStatsDto();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(List.of(mockViewStatsDto)))
                .addHeader("Content-Type", "application/json"));

        List<ViewStatsDto> result = viewClient.get(start, end, uris, unique);

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo(expectedMethod);
        assertThat(recordedRequest.getPath()).isEqualTo(expectedPath);
        assertThat(result).hasSize(expectedReturnSize);
    }

    @SneakyThrows
    @Test
    public void getWhenReturn400_ThenReturnError() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = List.of("/events/1", "/events/2");
        Boolean unique = true;
        mockBackEnd.enqueue(new MockResponse().setResponseCode(400)
                .addHeader("Content-Type", "application/json"));

        assertThrows(WebClientResponseException.class, () -> viewClient.get(start, end, uris, unique));
    }

}
