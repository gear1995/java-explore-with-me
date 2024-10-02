package ru.practicum.main.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.event.model.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@UtilityClass
public class CompilationMapper {
    public static Compilation toCompilation(final CompilationDto compilationDto) {
        if (compilationDto == null) {
            return null;
        }
        return Compilation.builder()
                .id(compilationDto.getId())
                .title(compilationDto.getTitle())
                .events(compilationDto.getEvents())
                .pinned(compilationDto.getPinned())
                .build();
    }

    public static Compilation toCompilation(final NewCompilationDto newCompilationDto, final List<Event> events) {
        if (newCompilationDto == null) {
            return null;
        }

        return Compilation.builder()
                .events(new HashSet<>(events))
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(final Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(compilation.getEvents())
                .pinned(compilation.getPinned())
                .build();
    }

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilationList) {
        if (compilationList.isEmpty()) {
            return null;
        }

        ArrayList<CompilationDto> compilationDtoList = new ArrayList<>();

        for (Compilation compilation : compilationList) {
            compilationDtoList.add(toCompilationDto(compilation));
        }

        return compilationDtoList;
    }
}
