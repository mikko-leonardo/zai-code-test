package com.zai.zaicodetest.weatherrequest;

public interface WeatherRequestService
{
    String sendWeatherReportRequest(String city, String unit);
    String buildRequestUrl(String city, String unit);
    String mapResponseToReport(String response);
}
