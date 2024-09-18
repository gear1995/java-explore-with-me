package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class HitDto {
    private Long id;
    private String app;
    private String uri;
    private Long hits;
}
