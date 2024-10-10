package ru.practicum.main.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Builder
@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    public NewCompilationDto(List<Long> events, Boolean pinned, String title) {
        this.events = events;
        this.title = title;
        this.pinned = Objects.requireNonNullElse(pinned, false);
    }
}
