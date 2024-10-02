package ru.practicum.main.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getCurrentUserRequests(@PathVariable Long userId) {
        return requestService.getCurrentUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Create request to event with id {} by user with id {}", eventId, userId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @RequestParam Long eventId, @PathVariable Long requestId) {
        log.info("Cancel request with id {}", requestId);
        return requestService.cancelRequestToEventByOwner(userId, eventId, requestId);
    }
}
