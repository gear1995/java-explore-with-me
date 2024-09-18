package ru.practicum.hit.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.hit.controller.repository.HitRepository;
import ru.practicum.hit.mapper.HitMapper;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private HitRepository hitRepository;

    @Override
    public HitDto createHit(HitDto hitDto) {
        return HitMapper.toHitDto(hitRepository.save(HitMapper.toHit(hitDto)));
    }
}
