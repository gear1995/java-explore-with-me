package ru.practicum.event.service;

import ru.practicum.event.dto.EventDto;

import java.util.List;

public interface EventService {
    List<EventDto> getEventsByParam(List<Long> users,
                                    List<String> states,
                                    List<Long> categories,
                                    String rangeStart,
                                    String rangeEnd,
                                    int from,
                                    int size);
}
