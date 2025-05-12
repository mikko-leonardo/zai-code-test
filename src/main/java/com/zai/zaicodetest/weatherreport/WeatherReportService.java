package com.zai.zaicodetest.weatherreport;

import com.google.gson.JsonObject;
import com.zai.zaicodetest.reportcache.CacheStore;
import com.zai.zaicodetest.reportcache.WeatherReport;
import com.zai.zaicodetest.weatherrequest.WeatherRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class WeatherReportService {

    @Autowired
    WeatherRequestService weatherStackRequestServiceImpl;

    @Autowired
    WeatherRequestService openWeatherRequestServiceImpl;

    @Autowired
    CacheStore<WeatherReport> weatherReportCache;

    public String getWeatherReport(String city, String unit)
    {
        //Check if there is a cached report that is still valid
        WeatherReport cachedReport = weatherReportCache.get("cachedReport");
        if (cachedReport != null && cachedReport.getTimestamp() > System.currentTimeMillis() - 1000 * 3) {
            System.out.println("Using cached report");
            return cachedReport.getReport().toString();
        }

        System.out.println("Getting new report");

        //If there's no cached report or it's not valid, try to get a new one using WeatherStack API
        JsonObject jsonReport = weatherStackRequestServiceImpl.sendWeatherReportRequest(city, unit);
        if (isReportValid(jsonReport)) {
            weatherReportCache.invalidate("cachedReport");
            weatherReportCache.put("cachedReport", new WeatherReport(jsonReport, System.currentTimeMillis()));
            return jsonReport.toString();
        } else {
            //If WeatherStack API fails, try to get a new one using OpenWeather API
            jsonReport = openWeatherRequestServiceImpl.sendWeatherReportRequest(city, unit);
            if (isReportValid(jsonReport)) {
                weatherReportCache.invalidate("cachedReport");
                weatherReportCache.put("cachedReport", new WeatherReport(jsonReport, System.currentTimeMillis()));
                return jsonReport.toString();
            }
        }

        //If all else fails, return the cached report if it exists
        if (cachedReport != null) {
            return cachedReport.getReport().toString();
        } else {
            //If there's no cached report, return an error message'
            return jsonReport.toString();
        }
    }

    private Boolean isReportValid(JsonObject report) {
        return report.has("wind_speed") && report.has("temperature_degrees");
    }

}
