package ru.practicum.hit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitDto;
import ru.practicum.hit.model.Hit;

import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class HitMapper {
    public Hit toHit(HitDto hitDto) {
        if (hitDto == null) {
            return null;
        }

        return Hit.builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .queried(hitDto.getQueried())
                .build();
    }

    public HitDto toHitDto(Hit hit) {
        if (hit == null) {
            return null;
        }

        return HitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .build();
    }

    public List<HitDto> toHitDtoList(List<Hit> hitList) {
        if (hitList == null) {
            return null;
        }
        List<HitDto> hitDtoList = new ArrayList<>();

        for (Hit hit : hitList) {
            hitDtoList.add(toHitDto(hit));
        }

        return hitDtoList;
    }
}
