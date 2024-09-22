package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EventMapper {
    public static Event toEvent(EventDto eventDto) {
        if (eventDto == null) {
            return null;
        }

        return Event.builder()
                .id(eventDto.getId())
                .eventDate(eventDto.getEventDate())
                .state(eventDto.getStateAction())
                .createdOn(eventDto.getCreatedOn())
                .views(eventDto.getViews())
                .confirmedRequests(eventDto.getConfirmedRequests())
                .initiator(UserMapper.toUser(eventDto.getInitiator()))
                .publishedOn(eventDto.getPublishedOn())
                .paid(eventDto.getPaid())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .requestModeration(eventDto.getRequestModeration())
                .category(eventDto.getCategory())
                .location(eventDto.getLocation())
                .participantLimit(eventDto.getParticipantLimit())
                .state(eventDto.getStateAction())
                .title(eventDto.getTitle())
                .build();
    }

    public static EventDto toEventDto(Event event) {
        if (event == null) {
            return null;
        }

        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(event.getCategory())
                .description(event.getDescription())
                .title(event.getTitle())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .stateAction(event.getState())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public static List<EventDto> toEventDtoList(List<Event> events) {
        if (events == null) {
            return null;
        }

        List<EventDto> eventDtoList = new ArrayList<>();

        for (Event event : events) {
            eventDtoList.add(toEventDto(event));
        }

        return eventDtoList;
    }
}
