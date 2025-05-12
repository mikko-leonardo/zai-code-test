package com.zai.zaicodetest.weatherreport;

import com.zai.zaicodetest.weatherrequest.WeatherRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class WeatherReportService {

    @Autowired
    private WeatherRequestService weatherStackRequestServiceImpl;

    @Autowired
    private WeatherRequestService openWeatherRequestServiceImpl;

    public String getWeatherReport(String city, String unit)
    {
        String report = weatherStackRequestServiceImpl.sendWeatherReportRequest(city, unit);

        //if request fails, try OpenWeatherRequestServiceImpl

        //openWeatherRequestServiceImpl.sendWeatherReportRequest(city, unit);

        //Return Report as JSON

        return report;
    }

}
