package com.zai.zaicodetest.weatherrequest;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class WeatherStackRequestServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherStackRequestServiceImpl weatherStackRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(weatherStackRequestService, "apiKey", "764520a89a55aad3e308cae83b3491af");
        ReflectionTestUtils.setField(weatherStackRequestService, "apiUrl", "api.weatherstack.com");
    }

    @Test
    void buildRequestUrl_MetricUnits_ReturnsCorrectUrl() {
        String url = weatherStackRequestService.buildRequestUrl("melbourne", "metric");
        assertTrue(url.contains("units=m"));
        assertTrue(url.contains("query=melbourne"));
        assertTrue(url.contains("access_key=764520a89a55aad3e308cae83b3491af"));
    }

    @Test
    void buildRequestUrl_ImperialUnits_ReturnsCorrectUrl() {
        String url = weatherStackRequestService.buildRequestUrl("sydney", "imperial");
        assertTrue(url.contains("units=f"));
        assertTrue(url.contains("query=sydney"));
    }

    @Test
    void buildRequestUrl_ScientificUnits_ReturnsCorrectUrl() {
        String url = weatherStackRequestService.buildRequestUrl("brisbane", "scientific");
        assertTrue(url.contains("units=s"));
        assertTrue(url.contains("query=brisbane"));
    }

    @Test
    void isResponseValid_ValidResponse_ReturnsTrue() {
        ResponseEntity<String> response = new ResponseEntity<>(
                "{\"current\": {\"temperature\": 20, \"wind_speed\": 10}}",
                HttpStatus.OK
        );
        assertTrue(weatherStackRequestService.isResponseValid(response));
    }

    @Test
    void isResponseValid_ErrorResponse_ReturnsFalse() {
        ResponseEntity<String> response = new ResponseEntity<>(
                "{\"error\": {\"code\": 101, \"message\": \"Invalid API key\"}}",
                HttpStatus.OK
        );
        assertFalse(weatherStackRequestService.isResponseValid(response));
    }

    @Test
    void mapResponseToReport_ValidResponse_ReturnsCorrectJson() {
        String response = "{\"current\": {\"temperature\": 20, \"wind_speed\": 10}}";
        JsonObject result = weatherStackRequestService.mapResponseToReport(response);

        assertNotNull(result.get("temperature_degrees"));
        assertNotNull(result.get("wind_speed"));
        assertEquals(20, result.get("temperature_degrees").getAsInt());
        assertEquals(10, result.get("wind_speed").getAsInt());
    }

    @Test
    void mapResponseToReport_InvalidResponse_ReturnsError() {
        String response = "invalid json";
        JsonObject result = weatherStackRequestService.mapResponseToReport(response);

        assertTrue(result.has("error"));
        assertEquals("something went wrong", result.get("error").getAsString());
    }

    @Test
    void sendWeatherReportRequest_SuccessfulRequest_ReturnsWeatherData() {
        String successResponse = "{\"current\": {\"temperature\": 20, \"wind_speed\": 10}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(successResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        JsonObject result = weatherStackRequestService.sendWeatherReportRequest("melbourne", "metric");

        assertNotNull(result);
        assertTrue(result.has("temperature_degrees"));
        assertTrue(result.has("wind_speed"));
    }
}