package ru.practicum.main.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.service.CompilationService;

@RestController
@RequestMapping(path = "admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Create new compilation");
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable long compId) {
        log.info("Delete compilation by id: {}", compId);
        compilationService.deleteCompilationById(compId);
    }

    @PatchMapping("{compId}")
    public CompilationDto updateCompilationById(@PathVariable long compId,
                                                @RequestBody @Valid CompilationDto compilationDto) {
        log.info("Update compilation with id {}", compId);
        return compilationService.updateCompilationById(compId, compilationDto);
    }
}
