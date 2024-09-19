package ru.practicum.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.dto.HitDto;
import ru.practicum.hit.client.HitClient;

@Controller
@RequestMapping(path = "hit")
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitClient hitClient;

    @PostMapping
    public ResponseEntity<Object> createHit(@RequestBody HitDto hitDto) {
        log.info("Creating hit: {}", hitDto.toString());

        return hitClient.createHit(hitDto);
    }
}
