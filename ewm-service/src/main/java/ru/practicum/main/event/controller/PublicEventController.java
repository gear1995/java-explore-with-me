package ru.practicum.main.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.SimpleEventDto;
import ru.practicum.main.event.model.SortValue;
import ru.practicum.main.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<SimpleEventDto> getEventsByParam(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                                 @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                 @RequestParam(required = false) SortValue sort,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 HttpServletRequest request) {
        log.info("Getting public events by params");
        if (text != null && (text.isEmpty() || text.length() > 7000)) {
            throw new DataIntegrityViolationException("Text length must be between 1 and 7000");
        }
        if (Objects.isNull(rangeStart)) {
            rangeStart = LocalDateTime.now();
        }
        if (Objects.isNull(rangeEnd)) {
            rangeEnd = LocalDateTime.now().plusYears(999);
        }
        if (rangeEnd.isBefore(rangeStart) || rangeEnd.equals(rangeStart)) {
            throw new DataIntegrityViolationException("Range end is before or equals to range start");
        }
        if (sort == null) {
            sort = SortValue.ID;
        }
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
