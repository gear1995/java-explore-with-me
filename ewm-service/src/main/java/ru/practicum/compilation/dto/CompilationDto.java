package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private Set<Event> events;
    private Boolean pinned;
    private String title;
}
