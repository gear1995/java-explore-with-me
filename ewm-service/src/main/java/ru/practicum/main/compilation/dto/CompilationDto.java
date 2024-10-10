package ru.practicum.main.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main.event.dto.SimpleEventDto;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class CompilationDto {
    private Long id;

    private List<SimpleEventDto> events;

    private Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
