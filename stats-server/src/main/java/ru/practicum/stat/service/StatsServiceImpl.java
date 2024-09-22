package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatDto;
import ru.practicum.stat.model.Stat;
import ru.practicum.stat.repository.StatsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public List<StatDto> getStats(Stat stat) {
        if (stat.isUnique()) {
            return statsRepository.getUniqueStatByParam(stat.getStart(), stat.getEnd(), stat.getUris());
        } else {
            return statsRepository.getAllStatByParam(stat.getStart(), stat.getEnd(), stat.getUris());
        }
    }
}
