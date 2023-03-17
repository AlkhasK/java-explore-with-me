package ru.practicum.server.hit.model;

import org.springframework.stereotype.Component;
import ru.practicum.dto.hit.HitCreateDto;

@Component
public class HitMapper {

    public Hit toHit(HitCreateDto hitCreateDto) {
        Hit hit = new Hit();
        hit.setApp(hitCreateDto.getApp());
        hit.setIp(hitCreateDto.getIp());
        hit.setUri(hitCreateDto.getUri());
        hit.setTimestamp(hitCreateDto.getTimestamp());
        return hit;
    }

}
