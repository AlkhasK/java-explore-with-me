package ru.practicum.main.event.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.location.model.Location;
import ru.practicum.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Embedded
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name = "title")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        return getId() != null ? getId().equals(event.getId()) : event.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : getClass().hashCode();
    }
}
