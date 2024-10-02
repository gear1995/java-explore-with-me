package ru.practicum.main.compilation.service;

import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilationsByParam(boolean pinned, int from, int size);

    CompilationDto getCompilationById(long compId);

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilationById(long compId);

    CompilationDto updateCompilationById(long compId, CompilationDto compilationDto);
}
