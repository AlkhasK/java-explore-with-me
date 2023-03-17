package ru.practicum.client.hit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.practicum.dto.hit.HitCreateDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HitClientTest {

    private static MockWebServer mockBackEnd;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HitClient hitClient;

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
        hitClient = new HitClient(baseUrl);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @SneakyThrows
    @Test
    public void postWhenValidData_ThenOk() {
        String expectedMethod = "POST";
        String expectedPath = "/hit";
        String app = "app";
        String uri = "uri";
        String ip = "1.1.1.1";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        HitCreateDto hitCreateDto = new HitCreateDto();
        hitCreateDto.setApp(app);
        hitCreateDto.setUri(uri);
        hitCreateDto.setIp(ip);
        hitCreateDto.setTimestamp(timestamp);
        String expectedBody = objectMapper.writeValueAsString(hitCreateDto);
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(200));

        hitClient.post(hitCreateDto);

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo(expectedMethod);
        assertThat(recordedRequest.getPath()).isEqualTo(expectedPath);
        assertThat(recordedRequest.getBody().readUtf8()).isEqualTo(expectedBody);
    }

    @SneakyThrows
    @Test
    public void postWhenReturn400_ThenReturnError() {
        String app = "app";
        String uri = "uri";
        String ip = "1.1.1.1";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        HitCreateDto hitCreateDto = new HitCreateDto();
        hitCreateDto.setApp(app);
        hitCreateDto.setUri(uri);
        hitCreateDto.setIp(ip);
        hitCreateDto.setTimestamp(timestamp);
        mockBackEnd.enqueue(new MockResponse().setResponseCode(400));

        assertThrows(WebClientResponseException.class, () -> hitClient.post(hitCreateDto));
    }
}
