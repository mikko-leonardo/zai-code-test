package com.zai.zaicodetest.weatherreport;

import com.google.gson.JsonObject;
import com.zai.zaicodetest.reportcache.CacheStore;
import com.zai.zaicodetest.reportcache.WeatherReport;
import com.zai.zaicodetest.weatherrequest.WeatherRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WeatherReportServiceTest {

    @Mock
    private WeatherRequestService weatherStackRequestServiceImpl;

    @Mock
    private WeatherRequestService openWeatherRequestServiceImpl;

    @Mock
    private CacheStore<WeatherReport> weatherReportCache;

    @InjectMocks
    private WeatherReportService weatherReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCachedReportWhenCacheIsValid() {
        // Arrange
        JsonObject reportData = new JsonObject();
        reportData.addProperty("wind_speed", "10");
        reportData.addProperty("temperature", "20");
        WeatherReport cachedReport = new WeatherReport(reportData, System.currentTimeMillis());

        when(weatherReportCache.get("cachedReport")).thenReturn(cachedReport);

        // Act
        String result = weatherReportService.getWeatherReport("melbourne", "metric");

        // Assert
        assertNotNull(result);
        verify(weatherStackRequestServiceImpl, never()).sendWeatherReportRequest(anyString(), anyString());
        verify(openWeatherRequestServiceImpl, never()).sendWeatherReportRequest(anyString(), anyString());
    }

    @Test
    void shouldFetchNewReportWhenCacheIsExpired() {
        // Arrange
        JsonObject newReport = new JsonObject();
        newReport.addProperty("temperature", "25");
        newReport.addProperty("wind_speed", "15");
        WeatherReport cachedReport = new WeatherReport(newReport, System.currentTimeMillis() - 5000);

        when(weatherReportCache.get("cachedReport")).thenReturn(cachedReport);
        when(weatherStackRequestServiceImpl.sendWeatherReportRequest("melbourne", "metric"))
                .thenReturn(newReport);

        // Act
        String result = weatherReportService.getWeatherReport("melbourne", "metric");

        // Assert
        assertNotNull(result);
        verify(weatherStackRequestServiceImpl).sendWeatherReportRequest("melbourne", "metric");
    }

    @Test
    void shouldFallbackToOpenWeatherWhenWeatherStackFails() {
        // Arrange
        JsonObject errorReport = new JsonObject();
        errorReport.addProperty("error", "API error");

        JsonObject successReport = new JsonObject();
        successReport.addProperty("temperature", "22");

        when(weatherReportCache.get("cachedReport")).thenReturn(null);
        when(weatherStackRequestServiceImpl.sendWeatherReportRequest("melbourne", "metric"))
                .thenReturn(errorReport);
        when(openWeatherRequestServiceImpl.sendWeatherReportRequest("melbourne", "metric"))
                .thenReturn(successReport);

        // Act
        String result = weatherReportService.getWeatherReport("melbourne", "metric");

        // Assert
        assertNotNull(result);
        verify(weatherStackRequestServiceImpl).sendWeatherReportRequest("melbourne", "metric");
        verify(openWeatherRequestServiceImpl).sendWeatherReportRequest("melbourne", "metric");
    }

    @Test
    void shouldReturnErrorWhenBothServicesFailWithError() {
        // Arrange
        JsonObject errorReport = new JsonObject();
        errorReport.addProperty("error", "API error");

        when(weatherReportCache.get("cachedReport")).thenReturn(null);
        when(weatherStackRequestServiceImpl.sendWeatherReportRequest("melbourne", "metric"))
                .thenReturn(errorReport);
        when(openWeatherRequestServiceImpl.sendWeatherReportRequest("melbourne", "metric"))
                .thenReturn(errorReport);

        // Act
        String result = weatherReportService.getWeatherReport("melbourne", "metric");

        // Assert
        assertTrue(result.contains("error"));
        verify(weatherStackRequestServiceImpl).sendWeatherReportRequest("melbourne", "metric");
        verify(openWeatherRequestServiceImpl).sendWeatherReportRequest("melbourne", "metric");
    }
}