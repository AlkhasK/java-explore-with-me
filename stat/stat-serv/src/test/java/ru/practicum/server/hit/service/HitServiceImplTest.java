package ru.practicum.server.hit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.hit.HitCreateDto;
import ru.practicum.server.hit.model.Hit;
import ru.practicum.server.hit.model.HitMapper;
import ru.practicum.server.hit.storage.HitRepository;

@ExtendWith(MockitoExtension.class)
public class HitServiceImplTest {

    @Mock
    private HitRepository hitRepository;

    @Mock
    private HitMapper hitMapper;

    @InjectMocks
    private HitServiceImpl hitService;

    @Test
    public void createHitWhenHitCreateDtoValid_ThenOk() {
        Hit hit = new Hit();
        HitCreateDto hitCreateDto = new HitCreateDto();
        Mockito.when(hitMapper.toHit(Mockito.any())).thenReturn(hit);

        hitService.createHit(hitCreateDto);

        Mockito.verify(hitRepository, Mockito.times(1)).save(Mockito.any());
    }
}
