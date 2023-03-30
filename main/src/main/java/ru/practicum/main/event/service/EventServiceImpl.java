package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.storage.CategoryStorage;
import ru.practicum.main.error.exception.EventConditionNotMetException;
import ru.practicum.main.error.exception.ObjectNotFoundException;
import ru.practicum.main.error.exception.ParticipationRequestParticipantLimitViolationException;
import ru.practicum.main.event.helper.parameters.AdminSearchEventsParameters;
import ru.practicum.main.event.helper.parameters.PublicSearchEventsParameters;
import ru.practicum.main.event.helper.validation.EventDateValidation;
import ru.practicum.main.event.helper.validation.StateValidation;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.model.dto.*;
import ru.practicum.main.event.model.dto.state.UpdateStatus;
import ru.practicum.main.event.model.parameters.SortType;
import ru.practicum.main.event.storage.EventStorage;
import ru.practicum.main.event.storage.Predicates;
import ru.practicum.main.participation.model.ParticipationRequest;
import ru.practicum.main.participation.model.Status;
import ru.practicum.main.participation.model.dto.ParticipationRequestDto;
import ru.practicum.main.participation.model.dto.ParticipationRequestMapper;
import ru.practicum.main.participation.storage.ParticipationRequestStorage;
import ru.practicum.main.participation.storage.helper.RequestCountByEvent;
import ru.practicum.main.stat.StatClient;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.storage.UserStorage;
import ru.practicum.main.utils.JsonPatch;
import ru.practicum.main.utils.pagination.PageRequestWithOffset;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventPrivateService, EventAdminService, EventPublicService {

    private static final int UNLIMITED_PARTICIPATION_LIMIT = 0;

    private final EventStorage eventStorage;
    private final EventMapper eventMapper;
    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;
    private final JsonPatch jsonPatch;
    private final StatClient statClient;
    private final ParticipationRequestStorage participationRequestStorage;
    private final ParticipationRequestMapper participationRequestMapper;

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Category category = categoryStorage.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Category with id=%s was not found", newEventDto.getCategory())));
        Event event = eventMapper.toEvent(newEventDto, category, initiator);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        Event createdEvent = eventStorage.save(event);
        return eventMapper.toEventFullDto(createdEvent);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Pageable pageable = PageRequestWithOffset.of(from, size);
        List<Event> events = eventStorage.findAllWithCategoryAndInitiator(userId, pageable);
        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventStorage.findWithCategoryAndInitiator(userId, eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        return eventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventStorage.findWithCategoryAndInitiator(userId, eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        StateValidation.validateEventUpdateUser(event.getState());
        Long categoryId = updateEventUserRequest.getCategory();
        Category category = categoryId == null ? null : categoryStorage.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Category with id=%s was not found", categoryId)));
        Event patchEvent = eventMapper.toEvent(updateEventUserRequest, category);
        Event updatedEvent = jsonPatch.mergePatch(event, patchEvent, Event.class);
        updatedEvent = eventStorage.save(updatedEvent);
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventFullDto> getEvents(AdminSearchEventsParameters parameters, Integer from, Integer size) {
        Pageable pageable = PageRequestWithOffset.of(from, size);
        List<Event> events = eventStorage.findAll((event, query, builder) -> {
            event.fetch("initiator");
            event.fetch("category");
            return builder.and(getFiltersAdminEvents(parameters, event, builder).toArray(new Predicate[]{}));
        }, pageable).getContent();
        List<EventFullDto> eventFullDtos = events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
        Map<Long, Long> eventHits = statClient.getHits(events, parameters.getRangeStart(), parameters.getRangeEnd());
        eventFullDtos.forEach(ent -> ent.setViews(eventHits.getOrDefault(ent.getId(), 0L)));
        Map<Long, Long> eventConfReq = getConfirmedRequestsCount(events);
        eventFullDtos.forEach(ent -> ent.setConfirmedRequests(eventConfReq.getOrDefault(ent.getId(), 0L)));
        return eventFullDtos;
    }

    private List<Predicate> getFiltersAdminEvents(AdminSearchEventsParameters parameters,
                                                  Root<Event> root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (parameters.getUsers() != null) {
            predicates.add(Predicates.hasInitiatorIn(root, parameters.getUsers()));
        }
        if (parameters.getStates() != null) {
            predicates.add(Predicates.hasStateIn(root, parameters.getStates()));
        }
        if (parameters.getCategories() != null) {
            predicates.add(Predicates.hasCategoryIn(root, parameters.getCategories()));
        }
        if (parameters.getRangeStart() != null) {
            predicates.add(Predicates.hasRangeStart(root, builder, parameters.getRangeStart()));
        }
        if (parameters.getRangeEnd() != null) {
            predicates.add(Predicates.hasRangeEnd(root, builder, parameters.getRangeEnd()));
        }
        return predicates;
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventStorage.findWithCategoryAndInitiator(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        Long categoryId = updateEventAdminRequest.getCategory();
        Category category = categoryId == null ? null : categoryStorage.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Category with id=%s was not found", categoryId)));
        Event patchEvent = eventMapper.toEvent(updateEventAdminRequest, category);
        StateValidation.validateEventUpdateAdmin(event.getState(), patchEvent.getState());
        Event updatedEvent = jsonPatch.mergePatch(event, patchEvent, Event.class);
        EventDateValidation.validateEventUpdateAdmin(updatedEvent.getEventDate());
        if (updatedEvent.getState() == State.PUBLISHED
                || (updatedEvent.getState() == State.PENDING
                && (updatedEvent.getParticipantLimit().equals(UNLIMITED_PARTICIPATION_LIMIT)
                || !updatedEvent.getRequestModeration()))
        ) {
            LocalDateTime publishOn = LocalDateTime.now();
            EventDateValidation.validateEventUpdateAdmin(updatedEvent.getEventDate(), publishOn);
            updatedEvent.setPublishedOn(publishOn);
        }
        updatedEvent = eventStorage.save(updatedEvent);
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventShortDto> getEvents(PublicSearchEventsParameters parameters, Integer from, Integer size) {
        if (parameters.getSortType() == null) {
            return getEventsUnsorted(parameters, from, size);
        } else if (parameters.getSortType() == SortType.EVENT_DATE) {
            return getEventsSortedEventDate(parameters, from, size);
        } else {
            return getEventsSortedViews(parameters, from, size);
        }
    }

    private List<EventShortDto> getEvents(PublicSearchEventsParameters parameters, Pageable pageable) {
        List<Event> events = eventStorage.findAll((event, query, builder) -> {
            event.fetch("initiator");
            event.fetch("category");
            return builder.and(getFiltersPublicEvents(parameters, event, builder).toArray(new Predicate[]{}));
        }, pageable).getContent();
        Map<Long, Long> eventHits = statClient.getHits(events, parameters.getRangeStart(), parameters.getRangeEnd());
        List<EventShortDto> eventShortDtos = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        eventShortDtos.forEach(ent -> ent.setViews(eventHits.getOrDefault(ent.getId(), 0L)));
        Map<Long, Long> eventConfReq = getConfirmedRequestsCount(events);
        eventShortDtos.forEach(ent -> ent.setConfirmedRequests(eventConfReq.getOrDefault(ent.getId(), 0L)));
        if (parameters.getOnlyAvailable()) {
            eventShortDtos = eventShortDtos.stream()
                    .filter(ent -> ent.getConfirmedRequests().equals(ent.getParticipantLimit().longValue()))
                    .collect(Collectors.toList());
        }
        return eventShortDtos;
    }

    private Map<Long, Long> getConfirmedRequestsCount(List<Event> events) {
        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<RequestCountByEvent> requestConfCount = participationRequestStorage
                .countRequestsForEvents(eventsIds, List.of(Status.CONFIRMED));
        return requestConfCount.stream()
                .collect(Collectors.toMap(RequestCountByEvent::getEventId, RequestCountByEvent::getCount,
                        (existing, replacement) -> existing));
    }

    private List<EventShortDto> getEventsUnsorted(PublicSearchEventsParameters parameters, Integer from, Integer size) {
        Pageable pageable = PageRequestWithOffset.of(from, size);
        return getEvents(parameters, pageable);
    }

    private List<EventShortDto> getEventsSortedEventDate(PublicSearchEventsParameters parameters, Integer from, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "eventDate");
        Pageable pageable = PageRequestWithOffset.of(from, size, sort);
        return getEvents(parameters, pageable);
    }

    private List<EventShortDto> getEventsSortedViews(PublicSearchEventsParameters parameters, Integer from, Integer size) {
        Pageable pageable = PageRequestWithOffset.of(from, size);
        List<EventShortDto> events = getEvents(parameters, pageable);
        return events.stream()
                .sorted(Comparator.comparing(EventShortDto::getViews))
                .collect(Collectors.toList());
    }

    private List<Predicate> getFiltersPublicEvents(PublicSearchEventsParameters parameters,
                                                   Root<Event> root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(Predicates.hasStatusPublished(root, builder));
        if (parameters.getText() != null) {
            predicates.add(Predicates.hasTextIgnoreCase(root, builder, parameters.getText()));
        }
        if (parameters.getCategories() != null) {
            predicates.add(Predicates.hasCategoryIn(root, parameters.getCategories()));
        }
        if (parameters.getPaid() != null) {
            predicates.add(Predicates.hasPaid(root, builder, parameters.getPaid()));
        }
        if (parameters.getRangeStart() != null) {
            predicates.add(Predicates.hasRangeStart(root, builder, parameters.getRangeStart()));
        } else {
            predicates.add(Predicates.hasRangeStart(root, builder, LocalDateTime.now()));
        }
        if (parameters.getRangeEnd() != null) {
            predicates.add(Predicates.hasRangeEnd(root, builder, parameters.getRangeEnd()));
        }
        return predicates;
    }

    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = eventStorage.findOne((eventRoot, query, builder) -> {
            eventRoot.fetch("initiator");
            eventRoot.fetch("category");
            return builder.and(Predicates.hasStatusPublished(eventRoot, builder),
                    Predicates.hasEventId(eventRoot, builder, eventId));
        }).orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setViews(statClient.getHit(eventFullDto.getId()));
        eventFullDto.setConfirmedRequests(getConfirmedRequestsCount(event));
        return eventFullDto;
    }

    private Long getConfirmedRequestsCount(Event event) {
        return participationRequestStorage
                .countAllByStatusInAndEvent_Id(List.of(Status.CONFIRMED), event.getId());
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        eventStorage.findWithCategoryAndInitiator(userId, eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        List<ParticipationRequest> participationRequests = participationRequestStorage.findAllWithEvent(eventId);
        return participationRequests.stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest updateRequest) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventStorage.findWithCategoryAndInitiator(userId, eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        List<ParticipationRequest> participationRequests = participationRequestStorage
                .findAllWithEventAndRequester(eventId, updateRequest.getRequestIds());
        if (updateRequest.getStatus() == UpdateStatus.CONFIRMED) {
            return updateRequestStatusConfirm(event, participationRequests);
        } else {
            return updateRequestStatusCancel(participationRequests);
        }
    }

    private EventRequestStatusUpdateResult updateRequestStatusConfirm(Event event,
                                                                      List<ParticipationRequest> participationRequests) {
        if (event.getParticipantLimit().equals(UNLIMITED_PARTICIPATION_LIMIT) || !event.getRequestModeration()) {
            List<ParticipationRequestDto> participationRequestDtos = participationRequests.stream()
                    .map(participationRequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
            eventRequestStatusUpdateResult.setConfirmedRequests(participationRequestDtos);
            return eventRequestStatusUpdateResult;
        }
        int participationRequestConfirmedCount = participationRequestStorage
                .countAllByStatusInAndEvent_Id(List.of(Status.CONFIRMED), event.getId()).intValue();
        int availableParticipationLimit = event.getParticipantLimit() - participationRequestConfirmedCount;
        if (availableParticipationLimit == 0) {
            throw new ParticipationRequestParticipantLimitViolationException("The participant limit has been reached");
        }
        if (availableParticipationLimit < participationRequests.size()) {
            participationRequests = participationRequests.subList(0, availableParticipationLimit);
        }
        validateParticipationRequestStatus(participationRequests);
        participationRequests.forEach(pr -> pr.setStatus(Status.CONFIRMED));
        participationRequestStorage.saveAll(participationRequests);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = participationRequests.stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmedRequests);
        if (availableParticipationLimit == participationRequests.size()) {
            List<ParticipationRequest> rejectedParticipationRequests = participationRequestStorage
                    .findAllByEventAndStatusesFetch(event.getId(), List.of(Status.PENDING));
            rejectedParticipationRequests.forEach(pr -> pr.setStatus(Status.REJECTED));
            participationRequestStorage.saveAll(rejectedParticipationRequests);
            List<ParticipationRequestDto> rejectedRequests = rejectedParticipationRequests.stream()
                    .map(participationRequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            eventRequestStatusUpdateResult.setRejectedRequests(rejectedRequests);
        }
        return eventRequestStatusUpdateResult;
    }

    private EventRequestStatusUpdateResult updateRequestStatusCancel(List<ParticipationRequest> participationRequests) {
        validateParticipationRequestStatus(participationRequests);
        participationRequests.forEach(pr -> pr.setStatus(Status.REJECTED));
        participationRequestStorage.saveAll(participationRequests);
        List<ParticipationRequestDto> participationRequestDtos = participationRequests.stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setRejectedRequests(participationRequestDtos);
        return eventRequestStatusUpdateResult;
    }


    private void validateParticipationRequestStatus(List<ParticipationRequest> participationRequests) {
        participationRequests.stream()
                .filter(pr -> pr.getStatus() != Status.PENDING).findFirst()
                .ifPresent(pr -> {
                    throw new EventConditionNotMetException("Request must have status PENDING");
                });
    }

}
