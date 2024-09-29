package ru.practicum.main.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main.request.model.RequestStatus;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class RequestStatusUpdateDto {
    private List<Long> requestIds;
    private RequestStatus status;
}
