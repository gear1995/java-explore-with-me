package ru.practicum.main.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.model.Request;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
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

    public static Request toRequest(RequestDto requestDto) {
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

    public static List<RequestDto> toRequestDtoList(List<Request> requestList) {
        if (requestList == null) {
            return null;
        }

        List<RequestDto> requestDtoList = new ArrayList<>();

        for (Request request : requestList) {
            requestDtoList.add(toRequestDto(request));
        }

        return requestDtoList;
    }
}
