package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@UtilityClass
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        if (request == null) {
            return null;
        }
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester())
                .created(request.getCreated())
                .event(request.getEvent())
                .status(request.getStatus())
                .build();
    }

    public Request toRequest(RequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return Request.builder()
                .id(requestDto.getId())
                .requester(requestDto.getRequester())
                .created(requestDto.getCreated())
                .event(requestDto.getEvent())
                .status(requestDto.getStatus())
                .build();
    }
}
