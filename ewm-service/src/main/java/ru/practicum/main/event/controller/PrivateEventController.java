package ru.practicum.main.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestStatusDto;
import ru.practicum.main.request.dto.RequestStatusUpdateDto;
import ru.practicum.main.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    public EventDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto event) {
        log.info("Creating event");

        if (event.getCreatedOn() == null) {
            event.setCreatedOn(LocalDateTime.now());
        }
        if (event.getRequestModeration()) {
            event.setRequestModeration(false);
        }
        if (event.getState() == null) {
            event.setState(EventState.PENDING);
        }

        return eventService.createEvent(userId, event);
    }

    @GetMapping
    public List<SimpleEventDto> getEventsByUserId(@PathVariable Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Getting events by userId {}", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("{eventId}")
    public EventDto getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Getting event by event id {}", eventId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventDto updateEventById(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody UpdateEventByUserDto updateEventByUserDto) {
        log.info("Updating event by eventId {}", eventId);
        return eventService.updateEventById(userId, eventId, updateEventByUserDto);
    }

    @GetMapping("{eventId}/requests")
    public List<RequestDto> getRequestsByEventOwner(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Getting requests by event owner");
        return requestService.getRequestsByEventOwner(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public RequestStatusDto updateRequests(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody RequestStatusUpdateDto requestStatusUpdateDto) {
        log.info("Updating requests by event owner");
        return requestService.updateRequests(userId, eventId, requestStatusUpdateDto);
    }
}
