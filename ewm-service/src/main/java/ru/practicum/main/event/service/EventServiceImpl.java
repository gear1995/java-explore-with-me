package ru.practicum.main.event.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.*;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.exeption.ValidationException;
import ru.practicum.main.statistics.StatisticsService;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.main.event.mapper.EventMapper.*;
import static ru.practicum.main.utils.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final StatisticsService statisticsService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventDto> getEventsByParam(List<Long> usersId,
                                           EventState states,
                                           List<Long> categoriesId,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Integer from,
                                           Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findUsersEventsByParams(usersId, categoriesId, states, rangeStart, rangeEnd, pageable)
                .stream()
                .map(event -> {
                    statisticsService.setView(event);
                    return EventMapper.toEventDto(event);
                })
                .collect(Collectors.toList());
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

        List<StatDto> stats = statisticsService.getStats(startTime, endTime, uris, true);
        stats.forEach(stat -> eventsUri.get(stat.getUri()).setViews(stat.getHits()));
    }

    @Override
    @Transactional
    public EventDto createEvent(long userId, NewEventDto eventDto) {
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category " + eventDto.getCategory() + " not found"));
        LocalDateTime eventDate = eventDto.getEventDate();
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataIntegrityViolationException("Event date " + eventDate + " is after event date " + LocalDateTime.now().plusHours(2));
        }
        Event event = toEvent(eventDto, category);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        event.setInitiator(user);
        event.setConfirmedRequests(0L);

        return toEventDto(eventRepository.save(event));
    }

    @Override
    public List<SimpleEventDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
        Pageable page = PageRequest.of(from / size, size);

        return toSimpleEventDtoList(eventRepository.findAllByInitiatorId(userId, page).toList());
    }

    @Override
    public EventDto getEventById(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

        return toEventDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found")));
    }

    @Override
    @Transactional
    public EventDto updateEventById(Long userId, Long eventId, UpdateEventByUserDto eventDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s or user with id %s not found", eventId, userId)));

        if (event.getPublishedOn() != null) {
            throw new ValidationException("Event is published, update denied");
        }

        if (eventDto == null) {
            return toEventDto(event);
        }

        if (eventDto.getAnnotation() != null) {
            int annotationLength = eventDto.getAnnotation().length();
            if (annotationLength < 20 || annotationLength > 7000) {
                throw new DataIntegrityViolationException(String.format("Annotation length: %d mus be in interval 20...7000",
                        annotationLength));
            }
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category width id " + eventDto.getCategory() + " not found"));
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            int descriptionLength = eventDto.getDescription().length();
            if (descriptionLength < 20 || descriptionLength > 7000) {
                throw new DataIntegrityViolationException(String.format("Description length: %d mus be in interval 20...7000",
                        descriptionLength));
            }
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
            int titleLength = eventDto.getTitle().length();
            if (titleLength < 3 || titleLength > 120) {
                throw new DataIntegrityViolationException(String.format("Title length: %d mus be in interval 3...120", titleLength));
            }
            event.setTitle(eventDto.getTitle());
        }

        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(UserStateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }

        return toEventDto(eventRepository.save(event));
    }

    @Override
    public List<SimpleEventDto> getPublicEventsByParam(String text,
                                                       List<Long> categories,
                                                       Boolean paid,
                                                       LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd,
                                                       Boolean onlyAvailable,
                                                       SortValue sort,
                                                       Integer from,
                                                       Integer size,
                                                       HttpServletRequest request) {
        String sorting;
        if (sort.equals(SortValue.EVENT_DATE)) {
            sorting = "eventDate";
        } else if (sort.equals(SortValue.VIEWS)) {
            sorting = "views";
        } else {
            sorting = "id";
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sorting));
        List<Event> sortedEvents = eventRepository.findEventsByParams(text, categories, paid, rangeStart, rangeEnd, pageable);
        if (onlyAvailable) {
            sortedEvents.removeIf(event -> Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit()));
        }
        statisticsService.sendStat(sortedEvents, request);
        eventRepository.saveAll(sortedEvents);

        return toSimpleEventDtoList(sortedEvents);
    }

    @Override
    public EventDto getPublicEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublishedOnIsNotNull(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
        statisticsService.setView(event);
        statisticsService.sendStat(event, request);

        return toEventDto(event);
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long eventId, UpdateEventByAdminDto updateEventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", eventId)));

        if (updateEventDto == null) {
            return toEventDto(event);
        }

        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id " + updateEventDto.getCategory() + " not found"));
            event.setCategory(category);
        }
        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }
        if (updateEventDto.getLocation() != null) {
            event.setLocation(updateEventDto.getLocation());
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }
        if (updateEventDto.getStateAction() != null) {
            if (updateEventDto.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new ValidationException("Event is already published");
                }
                if (event.getState().equals(EventState.CANCELED)) {
                    throw new ValidationException("Event is already canceled");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventDto.getStateAction().equals(AdminStateAction.REJECT_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new ValidationException("Event is already published");
                }
                event.setState(EventState.CANCELED);
            }
        }
        if (updateEventDto.getEventDate() != null) {
            LocalDateTime updateEventDate = updateEventDto.getEventDate();
            if (updateEventDate.isBefore(LocalDateTime.now())) {
                throw new DataIntegrityViolationException("Update event date must be before current moment.");
            }
            if (event.getPublishedOn() != null && updateEventDate.isBefore(event.getPublishedOn().plusHours(1))) {
                throw new DataIntegrityViolationException("The start date of the modified event must be no earlier" +
                                                          " than an hour from the publication date");
            }
            event.setEventDate(updateEventDto.getEventDate());
        }

        return toEventDto(eventRepository.save(event));
    }
}
