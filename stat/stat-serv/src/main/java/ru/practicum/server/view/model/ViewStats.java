package ru.practicum.server.view.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
