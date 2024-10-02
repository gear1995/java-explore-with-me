package ru.practicum.main.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.main.event.model.Event;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class CompilationDto {
    private Long id;

    private Set<Event> events;

    private Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
