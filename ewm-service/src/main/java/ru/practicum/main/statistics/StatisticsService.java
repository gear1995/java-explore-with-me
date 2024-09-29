package ru.practicum.main.statistics;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.StatDto;
import ru.practicum.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    void sendStat(Event event, HttpServletRequest request);

    void sendStat(List<Event> events, HttpServletRequest request);

    void sendStatForTheEvent(Long eventId, String remoteAddr, LocalDateTime now,
                             String nameService);

    void sendStatForEveryEvent(List<Event> events, String remoteAddr, LocalDateTime now,
                               String nameService);

    void setView(Event event);

    List<StatDto> getStats(String startTime, String endTime, List<String> uris);
}
