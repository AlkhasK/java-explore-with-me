package ru.practicum.main.participation.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.participation.model.ParticipationRequest;
import ru.practicum.main.participation.model.Status;
import ru.practicum.main.participation.storage.helper.RequestCountByEvent;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {

    @Query("select pr " +
            "from ParticipationRequest pr " +
            "JOIN FETCH pr.event " +
            "JOIN FETCH pr.requester " +
            "where pr.requester.id = :userId")
    List<ParticipationRequest> findAllWithRequester(@Param("userId") Long userId);

    Long countAllByStatusInAndEvent_Id(List<Status> statuses, Long eventId);

    Optional<ParticipationRequest> findByIdAndRequester_Id(Long requestId, Long userId);

    @Query("select pr " +
            "from ParticipationRequest pr " +
            "JOIN FETCH pr.event " +
            "JOIN FETCH pr.requester " +
            "where pr.event.id = :eventId")
    List<ParticipationRequest> findAllWithEvent(@Param("eventId") Long eventId);

    @Query("select pr " +
            "from ParticipationRequest pr " +
            "JOIN FETCH pr.event " +
            "JOIN FETCH pr.requester " +
            "where pr.event.id = :eventId and pr.id in :requestIds")
    List<ParticipationRequest> findAllWithEventAndRequester(@Param("eventId") Long eventId,
                                                            @Param("requestIds") List<Long> requestIds);

    @Query("select pr " +
            "from ParticipationRequest pr " +
            "JOIN FETCH pr.event " +
            "JOIN FETCH pr.requester " +
            "where pr.event.id = :eventId and pr.status in :statuses")
    List<ParticipationRequest> findAllByEventAndStatusesFetch(@Param("eventId") Long eventId,
                                                              @Param("statuses") List<Status> statuses);

    @Query("select new ru.practicum.main.participation.storage.helper.RequestCountByEvent(pr.event.id, count(pr)) " +
            "from ParticipationRequest pr " +
            "where pr.event.id in :eventIds " +
            "and pr.status in :statuses " +
            "group by pr.event.id")
    List<RequestCountByEvent> countRequestsForEvents(@Param("eventIds") List<Long> eventId,
                                                     @Param("statuses") List<Status> statuses);
}
