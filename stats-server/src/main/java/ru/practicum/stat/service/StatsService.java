package ru.practicum.stat.service;

import ru.practicum.dto.StatDto;
import ru.practicum.stat.model.Stat;

import java.util.List;

public interface StatsService {
    List<StatDto> getStats(Stat stat);
}
