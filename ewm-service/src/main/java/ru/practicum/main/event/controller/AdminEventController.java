package ru.practicum.main.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.UpdateEventByAdminDto;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@RestController
@RequestMapping(path = "admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEventsByParam(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) EventState states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get events by param");
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        return eventService.getEventsByParam(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("{eventId}")
    public EventDto updateEvent(@PathVariable Long eventId,
                                @RequestBody @Valid UpdateEventByAdminDto updateEventByAdminDto) {
        log.info("Update event");
        return eventService.updateEvent(eventId, updateEventByAdminDto);
    }
}
