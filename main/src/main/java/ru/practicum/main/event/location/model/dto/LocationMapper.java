package ru.practicum.main.event.location.model.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main.event.location.model.Location;

@Component
public class LocationMapper {

    public LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }

    public Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }
}
