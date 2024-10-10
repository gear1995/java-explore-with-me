package ru.practicum.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main.request.model.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private Long event;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime created;
    private Long requester;
    private RequestStatus status;
}
