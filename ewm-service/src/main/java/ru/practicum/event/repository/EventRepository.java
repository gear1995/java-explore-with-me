package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.dto.SimpleEventDto;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<SimpleEventDto> gerEventsByParam(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size);
}
