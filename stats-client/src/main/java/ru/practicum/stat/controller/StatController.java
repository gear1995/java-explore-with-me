package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.stat.client.StatsClient;

import java.util.List;

@Controller
@RequestMapping(path = "stats")
@RequiredArgsConstructor
@Slf4j
public class StatController {
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<Object> getStat(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam List<String> uris,
                                          @RequestParam(required = false) boolean unique) {
        log.info("Get stat");

        return statsClient.getStat(start, end, uris, unique);
    }
}
