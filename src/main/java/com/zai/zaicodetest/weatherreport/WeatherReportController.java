package com.zai.zaicodetest.weatherreport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
class WeatherReportController {

    @GetMapping("/weather")
    public void getWeatherReport(
            @RequestParam(defaultValue = "melbourne") String city,
            @RequestParam(defaultValue = "metric") String unit)
    {
        //Call Service for Weather Report Here
        //Return Report as JSON
    }

}
