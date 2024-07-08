package com.Necy.WeatherApp.service;

import com.Necy.WeatherApp.model.WeatherResponse;
import com.Necy.WeatherApp.util.ClientIpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class WeatherService {

   @Value("${ipinfo.api.key}")
    String ipInfoApiKey;
    @Value("${weatherstack.api.key}")
    private String apiKey;

    public WeatherResponse getWeatherResponse(String visitorName, HttpServletRequest request) throws IOException {
        // Get visitor's IP
        String clientIp = ClientIpUtil.getClientIp(request);

        // Get location based on IP
        String ipApiUrl = "https://ipinfo.io/" + clientIp + "?token=" + ipInfoApiKey;
        RestTemplate restTemplate = new RestTemplate();
        String ipResponse = restTemplate.getForObject(ipApiUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> ipInfo = objectMapper.readValue(ipResponse, new TypeReference<Map<String, Object>>() {});
        String city = (String) ipInfo.get("city");

        // Get weather condition/temperature
        String weatherApiUrl = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=" + city;
        String weatherResponse = restTemplate.getForObject(weatherApiUrl, String.class);

        Map<String, Object> weatherInfo = objectMapper.readValue(weatherResponse, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> currentInfo = objectMapper.convertValue(weatherInfo.get("current"), new TypeReference<Map<String, Object>>() {});
        //Double temperature = (Double) currentInfo.get("temp");
        Double temperature = ((Number) currentInfo.get("temperature")).doubleValue();
        // Return appropriate response
        WeatherResponse response = new WeatherResponse();
        response.setClientIp(clientIp);
        response.setLocation(city);
        response.setGreeting("Hello, " + visitorName + "!, the temperature is " + temperature + " degrees Celsius in " + city);

        return response;
    }
}
