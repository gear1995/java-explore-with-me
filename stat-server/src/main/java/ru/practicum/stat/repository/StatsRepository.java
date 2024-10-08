package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.StatDto;
import ru.practicum.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(value = "SELECT new ru.practicum.dto.StatDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
                   "FROM Hit as h " +
                   "WHERE h.queried BETWEEN :startDate AND :endDate " +
                   "AND (:uris IS NULL OR h.uri IN :uris) " +
                   "GROUP BY h.app, h.uri " +
                   "ORDER BY COUNT(h.ip) DESC")
    List<StatDto> getUniqueStatByParam(LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       List<String> uris);

    @Query(value = "SELECT new ru.practicum.dto.StatDto(h.app, h.uri, COUNT(h.ip)) " +
                   "FROM Hit as h " +
                   "WHERE h.queried BETWEEN :startDate AND :endDate " +
                   "AND (:uris IS NULL OR h.uri IN :uris) " +
                   "GROUP BY h.app, h.uri " +
                   "ORDER BY COUNT(h.ip) DESC")
    List<StatDto> getAllStatByParam(LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    List<String> uris);
}
