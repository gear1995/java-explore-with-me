package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilationsByParam(@RequestParam boolean pinned,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Get compilations by param from {} to {}", from, size);
        return compilationService.getCompilationsByParam(pinned, from, size);
    }

    @GetMapping({"compId"})
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Get compilation by id {}", compId);
        return compilationService.getCompilationById(compId);
    }
}
