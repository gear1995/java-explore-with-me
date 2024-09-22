package ru.practicum.request.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

@RestController
@RequestMapping("users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Create request to event with id {} by user with id {}", eventId, userId);
        return requestService.createRequest(userId, eventId);
    }
}
