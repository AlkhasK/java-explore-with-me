package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.compilation.model.dto.CompilationDto;
import ru.practicum.main.compilation.model.dto.CompilationMapper;
import ru.practicum.main.compilation.model.dto.NewCompilationDto;
import ru.practicum.main.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.main.compilation.storage.CompilationStorage;
import ru.practicum.main.compilation.storage.Predicates;
import ru.practicum.main.error.exception.ObjectNotFoundException;
import ru.practicum.main.event.storage.EventStorage;
import ru.practicum.main.utils.JsonPatch;
import ru.practicum.main.utils.pagination.PageRequestWithOffset;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationAdminService, CompilationPublicService {

    private final CompilationStorage compilationStorage;
    private final EventStorage eventStorage;
    private final CompilationMapper compilationMapper;
    private final JsonPatch jsonPatch;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation;
        Set<Long> eventIds = newCompilationDto.getEvents();
        if (eventIds == null || eventIds.isEmpty()) {
            compilation = compilationMapper.toCompilation(newCompilationDto, Collections.emptySet());
        } else {
            compilation = compilationMapper.toCompilation(newCompilationDto, eventStorage.findAllByIdIn(eventIds));
        }
        Compilation createdCompilation = compilationStorage.save(compilation);
        return compilationMapper.toCompilationDto(createdCompilation);
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationStorage.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Compilation with id=%s was not found", compId)));
        compilationStorage.delete(compilation);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationStorage.findByIdFetch(compId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Compilation with id=%s was not found", compId)));
        Compilation patchCompilation;
        Set<Long> eventIds = updateCompilationRequest.getEvents();
        if (eventIds == null || eventIds.isEmpty()) {
            patchCompilation = compilationMapper.toCompilation(compId, updateCompilationRequest, Collections.emptySet());
        } else {
            patchCompilation = compilationMapper.toCompilation(compId, updateCompilationRequest, eventStorage.findAllByIdIn(eventIds));
        }
        Compilation updatedCompilation = jsonPatch.mergePatch(compilation, patchCompilation, Compilation.class);
        updatedCompilation = compilationStorage.save(updatedCompilation);
        return compilationMapper.toCompilationDto(updatedCompilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequestWithOffset.of(from, size);
        List<Compilation> compilations = compilationStorage.findAll((compilation, query, builder) -> {
            compilation.fetch("events", JoinType.LEFT);
            List<Predicate> filters = pinned == null ? Collections.emptyList()
                    : List.of(Predicates.hasPinned(compilation, builder, pinned));
            return builder.and(filters.toArray(new Predicate[]{}));
        }, pageable).getContent();
        return compilations.stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationStorage.findByIdFetch(compId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Compilation with id=%s was not found", compId)));
        return compilationMapper.toCompilationDto(compilation);
    }
}
