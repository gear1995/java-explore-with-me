package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.hit.mapper.HitMapper;
import ru.practicum.stat.model.Stat;
import ru.practicum.stat.repository.StatsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public List<HitDto> getStats(Stat stat) {
        if (stat.isUnique()) {
            return HitMapper.toHitDtoList(statsRepository.getUniqueStatByParam(stat.getStart(), stat.getEnd(), stat.getUris()));
        } else {
            return HitMapper.toHitDtoList(statsRepository.getAllStatByParam(stat.getStart(), stat.getEnd(), stat.getUris()));
        }
    }
}
