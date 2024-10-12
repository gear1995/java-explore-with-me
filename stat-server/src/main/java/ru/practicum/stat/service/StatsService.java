package ru.practicum.stat.service;

import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
