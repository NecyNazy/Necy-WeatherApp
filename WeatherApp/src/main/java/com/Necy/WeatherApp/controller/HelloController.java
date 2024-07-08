package com.Necy.WeatherApp.controller;

import com.Necy.WeatherApp.model.WeatherResponse;
import com.Necy.WeatherApp.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HelloController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/api/hello")
    public WeatherResponse hello(@RequestParam(value = "visitor_name", defaultValue = "Guest")String visitorName, HttpServletRequest request){
        try{
            return weatherService.getWeatherResponse(visitorName, request);
        }catch (IOException e) {
             e.printStackTrace();
             return null;
        }
    }
}
