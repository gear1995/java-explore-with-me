package ru.practicum.main.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.SimpleEventDto;
import ru.practicum.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.user.mapper.UserMapper.toUserDto;

@UtilityClass
public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto, Category category) {
        if (newEventDto == null) {
            return null;
        }

        return Event.builder()
                .paid(newEventDto.getPaid())
                .eventDate(newEventDto.getEventDate())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .requestModeration(newEventDto.getRequestModeration())
                .category(category)
                .location(newEventDto.getLocation())
                .participantLimit(newEventDto.getParticipantLimit())
                .title(newEventDto.getTitle())
                .createdOn(LocalDateTime.now())
                .state(newEventDto.getState())
                .build();
    }

    public static EventDto toEventDto(Event event) {
        if (event == null) {
            return null;
        }

        return EventDto.builder()
                .id(event.getId())
                .createdOn(event.getCreatedOn())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(event.getCategory())
                .description(event.getDescription())
                .initiator(toUserDto(event.getInitiator()))
                .publishedOn(event.getPublishedOn())
                .title(event.getTitle())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public static List<SimpleEventDto> toSimpleEventDtoList(List<Event> eventList) {
        if (eventList == null) {
            return null;
        }

        List<SimpleEventDto> eventDtoList = new ArrayList<>();

        for (Event event : eventList) {
            eventDtoList.add(toSimpleEventDto(event));
        }

        return eventDtoList;
    }

    private static SimpleEventDto toSimpleEventDto(Event event) {
        if (event == null) {
            return null;
        }

        return SimpleEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .description(event.getDescription())
                .title(event.getTitle())
                .location(event.getLocation())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .paid(event.getPaid())
                .build();
    }
}
