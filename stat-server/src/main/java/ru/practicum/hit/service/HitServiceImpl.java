package ru.practicum.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.hit.repository.HitRepository;

import static ru.practicum.hit.mapper.HitMapper.toHit;
import static ru.practicum.hit.mapper.HitMapper.toHitDto;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Override
    public HitDto createHit(HitDto hitDto) {
        return toHitDto(hitRepository.save(toHit(hitDto)));
    }
}
