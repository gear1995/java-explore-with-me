package ru.practicum.main.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.model.SortValue;

import java.util.List;

public interface EventService {
    List<EventDto> getEventsByParam(List<Long> users,
                                    EventState states,
                                    List<Long> categories,
                                    String rangeStart,
                                    String rangeEnd,
                                    Integer from,
                                    Integer size);

    void setView(List<Event> events);

    EventDto createEvent(long userId, NewEventDto event);

    List<SimpleEventDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventDto getEventById(Long userId, Long eventId);

    EventDto updateEventById(Long userId, Long eventId, UpdateEventByUserDto newEventDto);

    List<EventDto> getPublicEventsByParam(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          Boolean onlyAvailable,
                                          SortValue sort,
                                          Integer from,
                                          Integer size, HttpServletRequest request);

    EventDto getPublicEventById(Long eventId, HttpServletRequest request);

    EventDto updateEvent(Long eventId, UpdateEventDto updateEventAdminDto);
}
