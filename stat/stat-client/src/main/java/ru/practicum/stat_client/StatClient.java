package ru.practicum.stat_client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.exception.StatsException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatClient {
    private final String serverUrl;
    private final RestTemplate restTemplate;

    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplate restTemplate) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
    }

    public void createHit(HitDto hitDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto, headers);
        restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, HitDto.class);
    }

    public List<StatDto> getStat(String start, String end, List<String> uris, Boolean unique) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("uris", uris);
        parameters.put("unique", unique);

        ResponseEntity<String> response = restTemplate.getForEntity(
                serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                String.class, parameters);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return Arrays.asList(objectMapper.readValue(response.getBody(), StatDto[].class));
        } catch (JsonProcessingException exception) {
            throw new StatsException(String.format("Json processing error: %s", exception.getMessage()));
        }
    }
}