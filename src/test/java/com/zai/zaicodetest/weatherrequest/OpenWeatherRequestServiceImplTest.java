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

class OpenWeatherRequestServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenWeatherRequestServiceImpl openWeatherRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(openWeatherRequestService, "apiKey", "testKey");
        ReflectionTestUtils.setField(openWeatherRequestService, "apiUrl", "api.openweathermap.org");
    }

    @Test
    void buildRequestUrl_ReturnsCorrectUrl() {
        String url = openWeatherRequestService.buildRequestUrl("melbourne", "metric");
        assertTrue(url.contains("units=metric"));
        assertTrue(url.contains("q=melbourne"));
        assertTrue(url.contains("appid=testKey"));
        assertTrue(url.startsWith("https://"));
    }

    @Test
    void isResponseValid_ValidResponse_ReturnsTrue() {
        ResponseEntity<String> response = new ResponseEntity<>(
                "{\"cod\": \"200\", \"main\": {\"temp\": \"20\"}, \"wind\": {\"speed\": \"10\"}}",
                HttpStatus.OK
        );
        assertTrue(openWeatherRequestService.isResponseValid(response));
    }

    @Test
    void isResponseValid_ErrorResponse_ReturnsFalse() {
        ResponseEntity<String> response = new ResponseEntity<>(
                "{\"cod\": \"404\", \"message\": \"city not found\"}",
                HttpStatus.NOT_FOUND
        );
        assertFalse(openWeatherRequestService.isResponseValid(response));
    }

    @Test
    void mapResponseToReport_ValidResponse_ReturnsCorrectJson() {
        String response = "{\"main\": {\"temp\": 20}, \"wind\": {\"speed\": 10}}";
        JsonObject result = openWeatherRequestService.mapResponseToReport(response);

        assertNotNull(result.get("temperature_degrees"));
        assertNotNull(result.get("wind_speed"));
        assertEquals(20, result.get("temperature_degrees").getAsInt());
        assertEquals(10, result.get("wind_speed").getAsInt());
    }

    @Test
    void mapResponseToReport_InvalidResponse_ReturnsError() {
        String response = "{\"invalid json\": \"force error\"}";
        JsonObject result = openWeatherRequestService.mapResponseToReport(response);

        assertTrue(result.has("error"));
        assertEquals("something went wrong", result.get("error").getAsString());
    }

    @Test
    void sendWeatherReportRequest_ErrorResponse_ReturnsErrorJson() {
        String errorResponse = "{\"cod\": \"404\", \"message\": \"city not found\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        JsonObject result = openWeatherRequestService.sendWeatherReportRequest("nonexistentcity", "metric");

        assertTrue(result.has("error"));
        assertEquals("something went wrong", result.get("error").getAsString());
    }
}