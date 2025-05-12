package com.zai.zaicodetest.weatherrequest;

public interface WeatherRequestService
{
    void sendWeatherReportRequest(String city, String unit);
    void buildRequestUrl(String city, String unit);
    void mapResponseToReport(String response);
}
