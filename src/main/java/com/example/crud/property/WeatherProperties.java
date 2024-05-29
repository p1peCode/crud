package com.example.crud.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "weather")
public class WeatherProperties {
    private String URL;
    private String API_KEY;
}
