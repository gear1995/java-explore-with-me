package ru.practicum.stat.mapper;

import lombok.experimental.UtilityClass;

import ru.practicum.dto.StatDto;
import ru.practicum.stat.model.Stat;

@UtilityClass
public class StatMapper {
    public StatDto toStatDto(Stat stat) {
        if (stat == null) {
            return null;
        }

        return StatDto.builder()
                .id(stat.getId())
                .start(stat.getStart())
                .end(stat.getEnd())
                .uris(stat.getUris())
                .unique(stat.isUnique())
                .build();
    }

    public Stat toStat(StatDto statDto) {
        if (statDto == null) {
            return null;
        }

        return Stat.builder()
                .id(statDto.getId())
                .start(statDto.getStart())
                .end(statDto.getEnd())
                .uris(statDto.getUris())
                .unique(statDto.isUnique())
                .build();
    }
}
