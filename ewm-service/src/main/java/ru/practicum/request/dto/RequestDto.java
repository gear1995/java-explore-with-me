package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.request.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private Long event;
    private LocalDateTime created;
    private Long requester;
    private RequestStatus status;
}
