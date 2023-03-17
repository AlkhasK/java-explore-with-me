package ru.practicum.dto.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class ViewStatsDto {
    @NotBlank
    @Size(max = 50)
    private String app;
    @NotBlank
    @URL
    private String uri;
    @NotNull
    private Long hits;
}
