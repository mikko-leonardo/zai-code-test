package com.zai.zaicodetest.weatherrequest;

import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;

public interface WeatherRequestService
{
    JsonObject sendWeatherReportRequest(String city, String unit);
    String buildRequestUrl(String city, String unit);
    JsonObject mapResponseToReport(String response);
    Boolean isResponseValid(ResponseEntity<String> response);
}
