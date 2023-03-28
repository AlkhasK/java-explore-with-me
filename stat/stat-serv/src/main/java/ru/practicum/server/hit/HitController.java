package ru.practicum.server.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.hit.HitCreateDto;
import ru.practicum.server.hit.service.HitService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
public class HitController {

    private final HitService hitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@Valid @RequestBody HitCreateDto hitCreateDto) {
        log.info("POST : create hit {}", hitCreateDto);
        hitService.createHit(hitCreateDto);
    }

}
