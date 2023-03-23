package ru.practicum.main.participation.service;

import ru.practicum.main.participation.model.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestPrivateService {
    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
