package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.exeption.NotFoundException;

import java.util.List;

import static ru.practicum.main.compilation.mapper.CompilationMapper.*;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilationsByParam(boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return toCompilationDtoList(compilationRepository.getByPinnedOrderByPinnedAsc(pinned, pageable));
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        return toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id " + compId + " not found")));
    }

    @Override
    public CompilationDto createCompilation(CompilationDto compilationDto) {
        return toCompilationDto(compilationRepository.save(toCompilation(compilationDto)));
    }

    @Override
    public void deleteCompilationById(long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id " + compId + " not found"));

        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationById(long compId, CompilationDto compilationDto) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id " + compId + " not found"));

        return toCompilationDto(compilationRepository.save(toCompilation(compilationDto)));
    }
}
