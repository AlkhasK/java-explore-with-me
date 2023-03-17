package ru.practicum.client.base;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class BaseClient {

    protected WebClient webClient;

    public BaseClient(WebClient webClient) {
        this.webClient = webClient;
    }

    protected <T> List<T> get(String path, MultiValueMap<String, String> params) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(params)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<T>>() {
                })
                .block();
    }

    protected <T> void post(String path, T t) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(t))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
