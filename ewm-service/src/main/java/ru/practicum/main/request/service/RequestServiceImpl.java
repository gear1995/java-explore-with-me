package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.exeption.ValidationException;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestStatusDto;
import ru.practicum.main.request.dto.RequestStatusUpdateDto;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.model.RequestStatus;
import ru.practicum.main.request.repository.RequestRepository;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.request.mapper.RequestMapper.toRequestDto;
import static ru.practicum.main.request.mapper.RequestMapper.toRequestDtoList;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));

        requestRepository.findByEventAndRequester(eventId, userId).ifPresent(request -> {
            throw new ValidationException("Can't repeated request");
        });

        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Can't create request by initiator");
        }

        if (event.getPublishedOn() == null) {
            throw new ValidationException("Event is not published yet");
        }

        List<Request> requests = requestRepository.findAllByEvent(eventId);

        if (!event.getRequestModeration() && requests.size() >= event.getParticipantLimit()) {
            throw new ValidationException("Member limit exceeded");
        }
        RequestStatus requestStatus = RequestStatus.PENDING;

        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            requestStatus = RequestStatus.CONFIRMED;
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .status(requestStatus)
                .requester(userId)
                .event(eventId)
                .build();

        return toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getRequestsByEventOwner(Long userId, Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

        return toRequestDtoList(requestRepository.findAllByEventWithInitiator(userId, eventId));
    }

    @Override
    public RequestStatusDto updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

        RequestStatusDto requestStatusDto = new RequestStatusDto();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return requestStatusDto;
        }

        List<Request> requests = requestRepository.findAllByEventWithInitiator(userId, eventId);
        List<Request> requestsToUpdate = requests
                .stream()
                .filter(request -> requestStatusUpdateDto.getRequestIds().contains(request.getId()))
                .collect(Collectors.toList());

        if (requestsToUpdate
                .stream()
                .anyMatch(request -> request.getStatus().equals(RequestStatus.CONFIRMED)
                                     && requestStatusUpdateDto.getStatus().equals(RequestStatus.REJECTED))) {
            throw new ValidationException("Request already confirmed");
        }

        if (event.getConfirmedRequests() + requestsToUpdate.size() > event.getParticipantLimit()
            && requestStatusUpdateDto.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new ValidationException("Exceeding the limit of participants");
        }

        for (Request request : requestsToUpdate) {
            request.setStatus(requestStatusUpdateDto.getStatus());
        }

        requestRepository.saveAll(requestsToUpdate);

        if (requestStatusUpdateDto.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + requestsToUpdate.size());
        }

        eventRepository.save(event);

        if (requestStatusUpdateDto.getStatus().equals(RequestStatus.CONFIRMED)) {
            requestStatusDto.setConfirmedRequests(toRequestDtoList(requestsToUpdate));
        }

        if (requestStatusUpdateDto.getStatus().equals(RequestStatus.REJECTED)) {
            requestStatusDto.setRejectedRequests(toRequestDtoList(requestsToUpdate));
        }

        return requestStatusDto;
    }

    @Override
    public List<RequestDto> getCurrentUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

        return toRequestDtoList(requestRepository.findAllByRequester(userId));
    }

    @Override
    public RequestDto cancelRequestToEventByOwner(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id " + requestId + " not found"));

        request.setStatus(RequestStatus.CANCELED);

        return toRequestDto(requestRepository.save(request));
    }
}
