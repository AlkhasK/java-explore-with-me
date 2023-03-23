package ru.practicum.main.compilation.model.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.dto.EventMapper;
import ru.practicum.main.event.model.dto.EventShortDto;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setEvents(events);
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        Set<EventShortDto> events = compilation.getEvents().stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toSet());
        compilationDto.setEvents(events);
        return compilationDto;
    }

    public Compilation toCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest,
                                     @Nullable Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setId(compId);
        compilation.setTitle(updateCompilationRequest.getTitle());
        compilation.setPinned(updateCompilationRequest.getPinned());
        compilation.setEvents(events);
        return compilation;
    }
}
