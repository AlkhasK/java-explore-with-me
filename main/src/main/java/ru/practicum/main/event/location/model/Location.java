package ru.practicum.main.event.location.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Location {
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
}
