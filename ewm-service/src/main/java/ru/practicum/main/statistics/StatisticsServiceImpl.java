package ru.practicum.main.statistics;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.stat.client.StatClient;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatClient statClient;

    @Override
    public void sendStat(Event event, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";

        HitDto requestDto = HitDto.builder()
                .app(nameService)
                .uri("/events")
                .ip(remoteAddr)
                .timestamp(LocalDateTime.parse(now.format(DATE_TIME_FORMATTER), DATE_TIME_FORMATTER))
                .build();

        statClient.createHit(requestDto);
        sendStatForTheEvent(event.getId(), remoteAddr, now, nameService);
    }

    @Override
    public void sendStat(List<Event> events, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";

        HitDto requestDto = HitDto.builder()
                .uri("/events")
                .app(nameService)
                .ip(remoteAddr)
                .timestamp(LocalDateTime.parse(now.format(DATE_TIME_FORMATTER), DATE_TIME_FORMATTER))
                .build();
        statClient.createHit(requestDto);
        sendStatForEveryEvent(events, remoteAddr, LocalDateTime.now(), nameService);
    }

    @Override
    public void sendStatForTheEvent(Long eventId,
                                    String remoteAddr,
                                    LocalDateTime now,
                                    String nameService) {
        HitDto requestHitDto = HitDto.builder()
                .app(nameService)
                .uri("/events/" + eventId)
                .ip(remoteAddr)
                .timestamp(LocalDateTime.parse(now.format(DATE_TIME_FORMATTER), DATE_TIME_FORMATTER))
                .build();

        statClient.createHit(requestHitDto);
    }

    @Override
    public void sendStatForEveryEvent(List<Event> events,
                                      String remoteAddr,
                                      LocalDateTime now,
                                      String nameService) {
        for (Event event : events) {
            HitDto requestHitDto = HitDto.builder()
                    .uri("/events/" + event.getId())
                    .app(nameService)
                    .ip(remoteAddr)
                    .timestamp(LocalDateTime.parse(now.format(DATE_TIME_FORMATTER), DATE_TIME_FORMATTER))
                    .build();

            statClient.createHit(requestHitDto);
        }
    }

    @Override
    public void setView(Event event) {
        String startTime = event.getCreatedOn().format(DATE_TIME_FORMATTER);
        String endTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        List<String> uris = List.of("/events/" + event.getId());

        List<StatDto> stats = getStats(startTime, endTime, uris, true);
        if (!stats.isEmpty()) {
            event.setViews(stats.getFirst().getHits());
        } else {
            event.setViews(0L);
        }
    }

    @Override
    public List<StatDto> getStats(String startTime, String endTime, List<String> uris, Boolean unique) {
        return statClient.getStat(startTime, endTime, uris, unique);
    }
}