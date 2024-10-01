package ru.practicum.main.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.*;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.statistics.StatisticsService;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EntityManager entityManager;
    private final StatisticsService statisticsService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventDto> getEventsByParam(List<Long> usersId,
                                           EventState states,
                                           List<Long> categoriesId,
                                           String rangeStart,
                                           String rangeEnd,
                                           Integer from,
                                           Integer size) {
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

        if (categoriesId != null && !categoriesId.isEmpty()) {
            Predicate containCategories = root.get("category").in(categoriesId);
            criteria = builder.and(criteria, containCategories);
        }

        if (usersId != null && !usersId.isEmpty()) {
            Predicate containUsers = root.get("initiator").in(usersId);
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

    @Override
    public void setView(List<Event> events) {
        LocalDateTime start = events.getFirst().getCreatedOn();
        List<String> uris = new ArrayList<>();
        Map<String, Event> eventsUri = new HashMap<>();
        String uri;
        for (Event event : events) {
            if (start.isBefore(event.getCreatedOn())) {
                start = event.getCreatedOn();
            }
            uri = "/events/" + event.getId();
            uris.add(uri);
            eventsUri.put(uri, event);
            event.setViews(0L);
        }

        String startTime = start.format(DATE_TIME_FORMATTER);
        String endTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        List<StatDto> stats = statisticsService.getStats(startTime, endTime, uris);
        stats.forEach(stat -> eventsUri.get(stat.getUri()).setViews(stat.getHits()));
    }

    @Override
    public EventDto createEvent(long userId, NewEventDto eventDto) {
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category " + eventDto.getCategory() + " not found"));
        LocalDateTime eventDate = eventDto.getEventDate();
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataIntegrityViolationException("Event date " + eventDate + " is after event date " + LocalDateTime.now().plusHours(2));
        }
        Event event = EventMapper.toEvent(eventDto, category);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        event.setInitiator(user);

        return EventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public List<SimpleEventDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
        Pageable page = PageRequest.of(from / size, size);

        return EventMapper.toSimpleEventDtoList(eventRepository.findAllByInitiatorId(userId, page).toList());
    }

    @Override
    public EventDto getEventById(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

        return EventMapper.toEventDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found")));
    }

    @Override
    public EventDto updateEventById(Long userId, Long eventId, UpdateEventByUserDto eventDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s or user with id %s not found", eventId, userId)));

        if (event.getPublishedOn() != null) {
            throw new DataIntegrityViolationException("Event is published, update denied");
        }

        if (eventDto == null) {
            return EventMapper.toEventDto(event);
        }

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category width id " + eventDto.getCategory() + " not found"));
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            LocalDateTime eventDateTime = eventDto.getEventDate();
            if (eventDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataIntegrityViolationException("The date and time for which the event is scheduled cannot be earlier" +
                                                          " than two hours from the current moment");
            }
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            Long participantLimit = eventDto.getParticipantLimit();
            if (participantLimit < 0) {
                throw new DataIntegrityViolationException("Participant limit is negative");
            }
            event.setParticipantLimit(participantLimit);
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }

        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(UserStateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }

        return EventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> getPublicEventsByParam(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 Boolean onlyAvailable,
                                                 SortValue sort,
                                                 Integer from,
                                                 Integer size, HttpServletRequest request) {
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

        if (text != null) {
            Predicate annotationContain = builder.like(builder.lower(root.get("annotation")),
                    "%" + text.toLowerCase() + "%");
            Predicate descriptionContain = builder.like(builder.lower(root.get("description")),
                    "%" + text.toLowerCase() + "%");
            Predicate containText = builder.or(annotationContain, descriptionContain);

            criteria = builder.and(criteria, containText);
        }

        if (categories != null && !categories.isEmpty()) {
            Predicate containStates = root.get("category").in(categories);
            criteria = builder.and(criteria, containStates);
        }

        if (paid != null) {
            Predicate isPaid;
            if (paid) {
                isPaid = builder.isTrue(root.get("paid"));
            } else {
                isPaid = builder.isFalse(root.get("paid"));
            }
            criteria = builder.and(criteria, isPaid);
        }

        if (rangeStart != null) {
            Predicate greaterTime = builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start);
            criteria = builder.and(criteria, greaterTime);
        }
        if (rangeEnd != null) {
            Predicate lessTime = builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end);
            criteria = builder.and(criteria, lessTime);
        }

        query.select(root).where(criteria).orderBy(builder.asc(root.get("eventDate")));
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (onlyAvailable) {
            events = events.stream()
                    .filter((event -> event.getConfirmedRequests() < (long) event.getParticipantLimit()))
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            if (sort.equals(SortValue.EVENT_DATE)) {
                events = events
                        .stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
            } else {
                events = events
                        .stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .collect(Collectors.toList());
            }
        }

        if (events.isEmpty()) {
            return new ArrayList<>();
        }

        setView(events);
        statisticsService.sendStat(events, request);

        return EventMapper.toEventDtoList(events);
    }

    @Override
    public EventDto getPublicEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublishedOnIsNotNull(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));

        statisticsService.setView(event);
        statisticsService.sendStat(event, request);

        return EventMapper.toEventDto(event);
    }

    @Override
    public EventDto updateEvent(Long eventId, UpdateEventDto updateEventAdminDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", eventId)));

        if (updateEventAdminDto == null) {
            return EventMapper.toEventDto(event);
        }

        if (updateEventAdminDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminDto.getAnnotation());
        }
        if (updateEventAdminDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id " + updateEventAdminDto.getCategory() + " not found"));
            event.setCategory(category);
        }
        if (updateEventAdminDto.getDescription() != null) {
            event.setDescription(updateEventAdminDto.getDescription());
        }
        if (updateEventAdminDto.getLocation() != null) {
            event.setLocation(updateEventAdminDto.getLocation());
        }
        if (updateEventAdminDto.getPaid() != null) {
            event.setPaid(updateEventAdminDto.getPaid());
        }
        if (updateEventAdminDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminDto.getParticipantLimit());
        }
        if (updateEventAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminDto.getRequestModeration());
        }
        if (updateEventAdminDto.getTitle() != null) {
            event.setTitle(updateEventAdminDto.getTitle());
        }
        if (updateEventAdminDto.getStateAction() != null) {
            if (updateEventAdminDto.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new DataIntegrityViolationException("Event is already published");
                }
                if (event.getState().equals(EventState.CANCELED)) {
                    throw new DataIntegrityViolationException("Event is already canceled");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminDto.getStateAction().equals(AdminStateAction.REJECT_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new DataIntegrityViolationException("Event is already published");
                }
                event.setState(EventState.CANCELED);
            }
        }
        if (updateEventAdminDto.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventAdminDto.getEventDate();
            if (eventDateTime.isBefore(LocalDateTime.now())
                || eventDateTime.isBefore(event.getPublishedOn().plusHours(1))) {
                throw new DataIntegrityViolationException("The start date of the event to be modified is less than one hour from the publication date.");
            }

            event.setEventDate(updateEventAdminDto.getEventDate());
        }

        return EventMapper.toEventDto(eventRepository.save(event));
    }
}
