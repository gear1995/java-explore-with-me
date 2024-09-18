package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(value = "SELECT h.app, h.uri, COUNT(DISTINCT h.ip) as hits " +
                   "FROM Hit as h " +
                   "WHERE h.queried BETWEEN :startDate AND :endDate AND h.uri IN :uris")
    List<Hit> getUniqueStatByParam(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   @Param("uris") List<String> uris);

    @Query(value = "SELECT h.app, h.uri, COUNT(h.ip) as hits " +
                   "FROM Hit as h " +
                   "WHERE h.queried BETWEEN :startDate AND :endDate AND h.uri IN :uris")
    List<Hit> getAllStatByParam(@Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                @Param("uris") List<String> uris);

}
