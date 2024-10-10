package ru.practicum.main.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilationsByParam(@RequestParam(required = false) boolean pinned,
                                                       @RequestParam(defaultValue = "0", required = false) int from,
                                                       @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Get compilations by param from {} to {}", from, size);
        return compilationService.getCompilationsByParam(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Get compilation by id {}", compId);
        return compilationService.getCompilationById(compId);
    }
}
