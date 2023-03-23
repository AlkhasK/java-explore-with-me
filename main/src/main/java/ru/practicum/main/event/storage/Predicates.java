package ru.practicum.main.event.storage;

import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class Predicates {
    public static Predicate hasInitiatorIn(Root<Event> root, List<Long> users) {
        return root.get("initiator").get("id").in(users);
    }

    public static Predicate hasStateIn(Root<Event> root, List<State> states) {
        return root.get("state").in(states);
    }

    public static Predicate hasCategoryIn(Root<Event> root, List<Long> categories) {
        return root.get("category").get("id").in(categories);
    }

    public static Predicate hasRangeStart(Root<Event> root, CriteriaBuilder cb, LocalDateTime rangeStart) {
        return cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
    }

    public static Predicate hasRangeEnd(Root<Event> root, CriteriaBuilder cb, LocalDateTime rangeEnd) {
        return cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
    }

    public static Predicate hasTextIgnoreCase(Root<Event> root, CriteriaBuilder cb, String text) {
        return cb.or(cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%"));
    }

    public static Predicate hasPaid(Root<Event> root, CriteriaBuilder cb, Boolean paid) {
        return cb.equal(root.get("paid"), paid);
    }

    public static Predicate hasStatusPublished(Root<Event> root, CriteriaBuilder cb) {
        return cb.equal(root.get("state"), State.PUBLISHED);
    }

    public static Predicate hasEventId(Root<Event> root, CriteriaBuilder cb, Long eventId) {
        return cb.equal(root.get("id"), eventId);
    }
}
