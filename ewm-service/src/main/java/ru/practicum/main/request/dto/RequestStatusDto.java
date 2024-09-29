package ru.practicum.main.request.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
