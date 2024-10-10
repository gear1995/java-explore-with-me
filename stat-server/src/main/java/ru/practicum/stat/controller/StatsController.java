package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatDto;
import ru.practicum.stat.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "stats")
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public List<StatDto> getStats(@RequestParam
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Getting stats");

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date is before start date");
        }

        if (uris != null) {
            List<String> obtainUris = new ArrayList<>();
            for (String uri : uris) {
                obtainUris.add(uri.replaceAll("\\[|\\]", ""));
            }
            return statsService.getStats(start, end, obtainUris, unique);
        } else {
            return statsService.getStats(start, end, null, unique);
        }
    }
}
