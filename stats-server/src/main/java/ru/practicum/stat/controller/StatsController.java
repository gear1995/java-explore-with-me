package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatDto;
import ru.practicum.stat.model.Stat;
import ru.practicum.stat.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "stats")
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public List<StatDto> getStats(@RequestParam String start,
                                  @RequestParam String end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(required = false) boolean unique) {
        log.info("Getting stats");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (uris != null && uris.isEmpty()) {
            uris = null;
        }
        return statsService.getStats(
                Stat.builder()
                        .start(LocalDateTime.parse(start, formatter))
                        .end(LocalDateTime.parse(end, formatter))
                        .uris(uris)
                        .unique(unique)
                        .build()
        );
    }
}
