package ru.practicum.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.SimpleEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.utils.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EntityManager entityManager;
    private final StatisticsService statisticsService;

    @Override
    public List<EventDto> getEventsByParam(List<Long> users,
                                           List<String> states,
                                           List<Long> categories,
                                           String rangeStart,
                                           String rangeEnd,
                                           int from,
                                           int size) {
        LocalDateTime start = rangeStart != null
                ? LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER)
                : null;

        LocalDateTime end = rangeEnd != null
                ? LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER)
                : null;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);

        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();

        if (categories != null && !categories.isEmpty()) {
            Predicate containCategories = root.get("category").in(categories);
            criteria = builder.and(criteria, containCategories);
        }

        if (users != null && !users.isEmpty()) {
            Predicate containUsers = root.get("initiator").in(users);
            criteria = builder.and(criteria, containUsers);
        }

        if (states != null) {
            Predicate containStates = root.get("state").in(states);
            criteria = builder.and(criteria, containStates);
        }

        if (rangeStart != null) {
            Predicate greaterTime = builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start);
            criteria = builder.and(criteria, greaterTime);
        }
        if (rangeEnd != null) {
            Predicate lessTime = builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end);
            criteria = builder.and(criteria, lessTime);
        }

        query.select(root).where(criteria);
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (events.isEmpty()) {
            return new ArrayList<>();
        }

        setView(events);
        return EventMapper.toEventDtoList(events);
    }
}
