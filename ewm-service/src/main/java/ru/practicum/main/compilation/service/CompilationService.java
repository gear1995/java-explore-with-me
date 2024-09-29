package ru.practicum.main.compilation.service;

import ru.practicum.main.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilationsByParam(boolean pinned, int from, int size);

    CompilationDto getCompilationById(long compId);

    CompilationDto createCompilation(CompilationDto compilationDto);

    void deleteCompilationById(long compId);

    CompilationDto updateCompilationById(long compId, CompilationDto compilationDto);
}
