package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.SortValue;
import ru.practicum.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEventsByParam(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
                                           @RequestParam(required = false) SortValue sort,
                                           @RequestParam(defaultValue = "0", required = false) Integer from,
                                           @RequestParam(defaultValue = "10", required = false) Integer size,
                                           HttpServletRequest request) {
        log.info("Getting public events by param");

        return eventService.getPublicEventsByParam(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request
        );
    }

    @GetMapping("{eventId}")
    public EventDto getPublicEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Getting public event by id {}", eventId);
        return eventService.getPublicEventById(eventId, request);
    }
}
