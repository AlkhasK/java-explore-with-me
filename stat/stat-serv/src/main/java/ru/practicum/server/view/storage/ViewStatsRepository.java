package ru.practicum.server.view.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.hit.model.Hit;
import ru.practicum.server.view.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface ViewStatsRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.server.view.model.ViewStats(hi.app, hi.uri, count(hi.ip)) " +
            "from Hit as hi " +
            "where hi.timestamp between :startDateTime and :endDateTime " +
            "and hi.uri in :uris " +
            "group by hi.app, hi.uri " +
            "order by count(hi.ip) desc ")
    List<ViewStats> getViewStatsByDatesAndUri(@Param("startDateTime") LocalDateTime startDateTime,
                                              @Param("endDateTime") LocalDateTime endDateTime,
                                              @Param("uris") List<String> uris);

    @Query("select new ru.practicum.server.view.model.ViewStats(hi.app, hi.uri, count(distinct hi.ip)) " +
            "from Hit as hi " +
            "where hi.timestamp between :startDateTime and :endDateTime " +
            "and hi.uri in :uris " +
            "group by hi.app, hi.uri " +
            "order by count(distinct hi.ip) desc ")
    List<ViewStats> getViewStatsByDatesAndUriUniqueIp(@Param("startDateTime") LocalDateTime startDateTime,
                                                      @Param("endDateTime") LocalDateTime endDateTime,
                                                      @Param("uris") List<String> uris);

    @Query("select new ru.practicum.server.view.model.ViewStats(hi.app, hi.uri, count(hi.ip)) " +
            "from Hit as hi " +
            "where hi.timestamp between :startDateTime and :endDateTime " +
            "group by hi.app, hi.uri " +
            "order by count(hi.ip) desc ")
    List<ViewStats> getViewStatsByDates(@Param("startDateTime") LocalDateTime startDateTime,
                                        @Param("endDateTime") LocalDateTime endDateTime);

    @Query("select new ru.practicum.server.view.model.ViewStats(hi.app, hi.uri, count(distinct hi.ip)) " +
            "from Hit as hi " +
            "where hi.timestamp between :startDateTime and :endDateTime " +
            "group by hi.app, hi.uri " +
            "order by count(distinct hi.ip) desc ")
    List<ViewStats> getViewStatsByDatesUniqueIp(@Param("startDateTime") LocalDateTime startDateTime,
                                                @Param("endDateTime") LocalDateTime endDateTime);
}
