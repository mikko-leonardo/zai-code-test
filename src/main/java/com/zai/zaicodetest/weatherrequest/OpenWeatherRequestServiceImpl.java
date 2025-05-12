package com.zai.zaicodetest.weatherrequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class OpenWeatherRequestServiceImpl implements WeatherRequestService
{
    @Value("${openweather.apiKey}")
    private String apiKey;

    @Value("${openweather.apiUrl}")
    private String apiUrl;

    @Override
    public String sendWeatherReportRequest(String city, String unit)
    {
        return "";
    }

    @Override
    public String buildRequestUrl(String city, String unit)
    {
        return "";
    }

    @Override
    public String mapResponseToReport(String response)
    {
        return "";
    }
}
