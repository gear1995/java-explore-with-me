package ru.practicum.stat.service;

import ru.practicum.dto.HitDto;
import ru.practicum.stat.model.Stat;

import java.util.List;

public interface StatsService {
    List<HitDto> getStats(Stat stat);
}
