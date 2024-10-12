package ru.practicum.hit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitDto;
import ru.practicum.hit.model.Hit;

@UtilityClass
public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        if (hitDto == null) {
            return null;
        }

        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .queried(hitDto.getTimestamp())
                .build();
    }

    public static HitDto toHitDto(Hit hit) {
        if (hit == null) {
            return null;
        }

        return HitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getQueried())
                .build();
    }
}
