package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.SimpleEventDto;
import ru.practicum.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEventsByParam(@RequestParam List<Long> users,
                                           @RequestParam List<String> states,
                                           @RequestParam List<Long> categories,
                                           @RequestParam String rangeStart,
                                           @RequestParam String rangeEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Get events by param");
        return eventService.getEventsByParam(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
