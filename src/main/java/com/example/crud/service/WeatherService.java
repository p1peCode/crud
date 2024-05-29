package com.example.crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final String apiKey = "54ca9de8-9b8c-4792-8685-d2ede53ae3c5";

    private final String URL = "https://api.weather.yandex.ru/graphql/query";

    public boolean isHumidity(String coordinates) throws JsonProcessingException {
        String[] partsOfCoordinates = coordinates.split(" ");
        String lat = partsOfCoordinates[1];
        String lon = partsOfCoordinates[0];
        String query = String.format("{\"query\":\"{\\n weatherByPoint(request: { lat: %s, lon: %s}) {\\n now {\\n humidity\\n }\\n }\\n}\\n\"}", lat, lon);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Yandex-Weather-Key", apiKey);
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(query, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

        String json = response.getBody();
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        int humidity = jsonNode.at("/data/weatherByPoint/now/humidity").asInt();

        return humidity > 70;
    }
}