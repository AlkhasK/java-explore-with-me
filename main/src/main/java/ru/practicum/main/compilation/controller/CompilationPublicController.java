package ru.practicum.main.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.model.dto.CompilationDto;
import ru.practicum.main.compilation.service.CompilationPublicService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationPublicService compilationService;

    @GetMapping
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET : find all compilation pinned : {}", pinned);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto find(@PathVariable Long compId) {
        log.info("GET : find compilation id : {}", compId);
        return compilationService.getCompilation(compId);
    }
}

