package ru.practicum.dto.hit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.valid.ip.Ip;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class HitCreateDto {
    @NotBlank
    @Size(max = 50)
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    @Ip
    private String ip;
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
