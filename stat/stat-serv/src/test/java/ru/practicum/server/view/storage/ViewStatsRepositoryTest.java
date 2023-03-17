package ru.practicum.server.view.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.server.hit.model.Hit;
import ru.practicum.server.view.model.ViewStats;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ViewStatsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ViewStatsRepository viewStatsRepository;

    private Hit hitFirst;
    private Hit hitSecond;
    private Hit hitThird;

    @BeforeEach
    public void initHitFirst() {
        String app = "ewm-main-service";
        String uri = "/events/1";
        String ip = "200.250.10.181";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        hitFirst = new Hit();
        hitFirst.setApp(app);
        hitFirst.setUri(uri);
        hitFirst.setIp(ip);
        hitFirst.setTimestamp(timestamp);
    }

    @BeforeEach
    public void initHitSecond() {
        String app = "ewm-main-service";
        String uri = "/events/1";
        String ip = "200.250.10.181";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusDays(1);
        hitSecond = new Hit();
        hitSecond.setApp(app);
        hitSecond.setUri(uri);
        hitSecond.setIp(ip);
        hitSecond.setTimestamp(timestamp);
    }

    @BeforeEach
    public void initHitThird() {
        String app = "ewm-main-another";
        String uri = "/events/2";
        String ip = "200.250.10.182";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusDays(2);
        hitThird = new Hit();
        hitThird.setApp(app);
        hitThird.setUri(uri);
        hitThird.setIp(ip);
        hitThird.setTimestamp(timestamp);
    }

    @Test
    public void getViewStatsByDatesAndUriWhenDataExists_ThenReturnViewStats() {
        int expectedResultSize = 1;
        int expectedHits = 2;
        entityManager.persistAndFlush(hitFirst);
        entityManager.persistAndFlush(hitSecond);
        entityManager.persistAndFlush(hitThird);

        LocalDateTime start = LocalDateTime.now().minusDays(3).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);
        List<String> uris = List.of("/events/1");

        List<ViewStats> result = viewStatsRepository.getViewStatsByDatesAndUri(start, end, uris);

        assertThat(result).hasSize(expectedResultSize);
        ViewStats viewStats = result.get(0);
        assertThat(viewStats.getHits()).isEqualTo(expectedHits);
    }

    @Test
    public void getViewStatsByDatesAndUriUniqueIpWhenDataExists_ThenReturnViewStats() {
        int expectedResultSize = 1;
        int expectedHits = 1;
        entityManager.persistAndFlush(hitFirst);
        entityManager.persistAndFlush(hitSecond);
        entityManager.persistAndFlush(hitThird);

        LocalDateTime start = LocalDateTime.now().minusDays(3).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);
        List<String> uris = List.of("/events/1");

        List<ViewStats> result = viewStatsRepository.getViewStatsByDatesAndUriUniqueIp(start, end, uris);

        assertThat(result).hasSize(expectedResultSize);
        ViewStats viewStats = result.get(0);
        assertThat(viewStats.getHits()).isEqualTo(expectedHits);
    }

    @Test
    public void getViewStatsByDatesWhenDataExists_ThenReturnViewStats() {
        int expectedResultSize = 2;
        int expectedHits = 2;
        entityManager.persistAndFlush(hitFirst);
        entityManager.persistAndFlush(hitSecond);
        entityManager.persistAndFlush(hitThird);

        LocalDateTime start = LocalDateTime.now().minusDays(3).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);

        List<ViewStats> result = viewStatsRepository.getViewStatsByDates(start, end);

        assertThat(result).hasSize(expectedResultSize);
        ViewStats viewStats = result.get(0);
        assertThat(viewStats.getHits()).isEqualTo(expectedHits);
    }

    @Test
    public void getViewStatsByDatesUniqueIpWhenDataExists_ThenReturnViewStats() {
        int expectedResultSize = 2;
        int expectedHits = 1;
        entityManager.persistAndFlush(hitFirst);
        entityManager.persistAndFlush(hitSecond);
        entityManager.persistAndFlush(hitThird);

        LocalDateTime start = LocalDateTime.now().minusDays(3).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);

        List<ViewStats> result = viewStatsRepository.getViewStatsByDatesUniqueIp(start, end);

        assertThat(result).hasSize(expectedResultSize);
        ViewStats viewStats = result.get(0);
        assertThat(viewStats.getHits()).isEqualTo(expectedHits);
    }
}
