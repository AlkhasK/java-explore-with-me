package ru.practicum.server.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.hit.HitCreateDto;
import ru.practicum.server.hit.model.Hit;
import ru.practicum.server.hit.model.HitMapper;
import ru.practicum.server.hit.storage.HitRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    private final HitMapper hitMapper;

    @Override
    @Transactional
    public void createHit(HitCreateDto hitCreateDto) {
        Hit hit = hitMapper.toHit(hitCreateDto);
        hitRepository.save(hit);
    }

}
