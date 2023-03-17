package ru.practicum.server.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void createHit(@Valid @RequestBody HitCreateDto hitCreateDto) {
        log.info("POST : create hit {}", hitCreateDto);
        hitService.createHit(hitCreateDto);
    }

}
