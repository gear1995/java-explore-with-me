package ru.practicum.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.hit.service.HitService;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitService hitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto postHit(@RequestBody HitDto hitDto) {
        log.info(hitDto.toString());
        if (hitDto.getQueried() == null) {
            hitDto.setQueried(LocalDateTime.now());
        }
        return hitService.createHit(hitDto);
    }
}
