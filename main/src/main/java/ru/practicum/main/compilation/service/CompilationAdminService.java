package ru.practicum.main.compilation.service;

import ru.practicum.main.compilation.model.dto.CompilationDto;
import ru.practicum.main.compilation.model.dto.NewCompilationDto;
import ru.practicum.main.compilation.model.dto.UpdateCompilationRequest;

public interface CompilationAdminService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
