package com.zai.zaicodetest.weatherreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
class WeatherReportController {

    @Autowired
    private WeatherReportService weatherReportService;

    @GetMapping(value = "/weather", produces = "application/json")
    public String getWeatherReport(
            @RequestParam(defaultValue = "melbourne") String city,
            @RequestParam(defaultValue = "metric") String unit)
    {
        return weatherReportService.getWeatherReport(city, unit);
    }

}
