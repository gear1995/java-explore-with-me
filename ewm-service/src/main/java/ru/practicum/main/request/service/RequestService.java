package ru.practicum.main.request.service;

import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestStatusDto;
import ru.practicum.main.request.dto.RequestStatusUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestsByEventOwner(Long userId, Long eventId);

    RequestStatusDto updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto);

    List<RequestDto> getCurrentUserRequests(Long userId);

    RequestDto cancelRequestToEventByOwner(Long userId, Long requestId);
}
