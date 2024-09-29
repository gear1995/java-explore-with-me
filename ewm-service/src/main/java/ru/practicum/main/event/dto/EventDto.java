package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.user.dto.UserDto;

import java.time.LocalDateTime;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
public class EventDto {
    private Long id;
    @Size(min = 20, max = 2000)
    private String annotation;
    private Category category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private EventState stateAction;
    @Size(min = 3, max = 120)
    private String title;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private UserDto initiator;
    private LocalDateTime publishedOn;
    private Long views;
//    private EventState state;
}
