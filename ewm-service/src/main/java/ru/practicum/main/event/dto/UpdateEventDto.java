package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.event.model.AdminStateAction;
import ru.practicum.main.event.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateEventDto {
    @NotBlank(message = "annotation must not be blank")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank(message = "description must not be blank")
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private LocalDateTime createdOn;

    @NotNull
    private Location location;

    private Boolean paid;

    @Positive
    private Long participantLimit;

    private Boolean requestModeration;

    @NotNull
    @Size(min = 3, max = 120)
    private String title;

    private AdminStateAction stateAction;
}
