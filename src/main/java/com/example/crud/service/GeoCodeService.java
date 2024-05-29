package com.example.crud.service;

import com.example.crud.property.GeoCodeProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoCodeService {

    @Autowired
    private final GeoCodeProperties geoCodeProperties;

    public GeoCodeService(GeoCodeProperties geoCodeProperties) {
        this.geoCodeProperties = geoCodeProperties;
    }

    public String getCoordinates(String address) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String URL = geoCodeProperties.getURL();
        String API_KEY = geoCodeProperties.getAPI_KEY();
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