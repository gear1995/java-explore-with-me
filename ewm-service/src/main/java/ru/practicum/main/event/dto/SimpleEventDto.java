package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEventDto {
    private Long id;
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Category category;
    @NotNull
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private EventState stateAction;
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}