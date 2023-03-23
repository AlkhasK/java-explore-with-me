package ru.practicum.main.compilation.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "compilation")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "compilation_event", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "compilation_title")
    private String title;
}
