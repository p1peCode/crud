package com.example.crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoCodeService {
    private final String API_KEY = "f8cff443-fab0-44b7-8533-d3868ca1f561";
    private final String URL = "https://geocode-maps.yandex.ru/1.x/";

    public String getCoordinates(String address) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        StringBuilder urlBuilder = new StringBuilder(URL);
        urlBuilder.append("?apikey=").append(API_KEY)
                .append("&geocode=").append(address)
                .append("&lang=").append("ru_RU")
                .append("&format=").append("json")
                .append("&results=").append(1);

        String url = urlBuilder.toString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String json = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        return jsonNode.at("/response/GeoObjectCollection/featureMember/0/GeoObject/Point/pos").asText();
    }
}