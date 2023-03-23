package ru.practicum.main.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.participation.model.dto.ParticipationRequestDto;
import ru.practicum.main.participation.service.ParticipationRequestPrivateService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestPrivateController {

    private final ParticipationRequestPrivateService participationRequestService;

    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable Long userId) {
        log.info("GET : user id : {} find all participation requests", userId);
        return participationRequestService.getParticipationRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                              @RequestParam Long eventId) {
        log.info("POST : user id : {} create participation request event id : {}", userId, eventId);
        return participationRequestService.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId,
                                                              @PathVariable Long requestId) {
        log.info("PATCH : user id : {} cancel participation request id : {}", userId, requestId);
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }
}
