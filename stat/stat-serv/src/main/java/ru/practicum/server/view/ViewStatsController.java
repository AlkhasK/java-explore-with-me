package ru.practicum.server.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.view.ViewStatsDto;
import ru.practicum.server.view.service.ViewStatsService;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/stats")
@RequiredArgsConstructor
@Validated
public class ViewStatsController {

    private final ViewStatsService viewStatsService;

    @GetMapping
    public List<ViewStatsDto> find(@PastOrPresent @RequestParam(name = "start")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(name = "uris") Optional<List<String>> uris,
                                   @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("GET : ViewStats start : {} end : {} uris : {} unique : {}", start, end, uris, unique);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(String.format(
                    "Start date should be before end date current start : %s end : %s", start, end));
        }
        return viewStatsService.getViewStatsDto(start, end, uris, unique);
    }

}
