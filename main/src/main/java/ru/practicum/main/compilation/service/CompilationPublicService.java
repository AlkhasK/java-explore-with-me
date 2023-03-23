package ru.practicum.main.compilation.service;

import ru.practicum.main.compilation.model.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);
}
