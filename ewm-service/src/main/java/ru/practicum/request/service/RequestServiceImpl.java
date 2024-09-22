package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Can't create request by initiator");
        }

        if (event.getPublishedOn() == null) {
            throw new NotFoundException("Event is not published yet");
        }

        List<Request> requests = requestRepository.findAllByEvent(eventId);

        if (!event.getRequestModeration() && requests.size() >= event.getParticipantLimit()) {
            throw new NotFoundException("Member limit exceeded");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .requester(userId)
                .event(eventId)
                .build();

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
