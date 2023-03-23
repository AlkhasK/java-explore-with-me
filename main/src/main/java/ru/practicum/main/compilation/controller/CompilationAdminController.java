package ru.practicum.main.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.model.dto.CompilationDto;
import ru.practicum.main.compilation.model.dto.NewCompilationDto;
import ru.practicum.main.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.main.compilation.service.CompilationAdminService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationAdminService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST : create compilation : {}", newCompilationDto);
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.info("DELETE : delete category id : {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("PATCH : update compilation id : {} value : {}", compId, updateCompilationRequest);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
