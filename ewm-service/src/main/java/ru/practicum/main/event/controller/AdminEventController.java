package ru.practicum.main.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.service.EventService;

import java.util.List;

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
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get events by param");
        return eventService.getEventsByParam(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("{eventId}")
    public EventDto updateEvent(@PathVariable Long eventId,
                                @Valid @RequestBody UpdateEventDto updateEventAdminDto) {
        log.info("Update event");
        return eventService.updateEvent(eventId, updateEventAdminDto);
    }
}
