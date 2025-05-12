package com.zai.zaicodetest.weatherrequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
class OpenWeatherRequestServiceImpl implements WeatherRequestService
{
    @Value("${openweather.apiKey}")
    private String apiKey;

    @Value("${openweather.apiUrl}")
    private String apiUrl;

    @Override
    public JsonObject sendWeatherReportRequest(String city, String unit)
    {
        String uri = buildRequestUrl(city, unit);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            return mapResponseToReport(response.getBody());

        } catch (HttpClientErrorException e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "something went wrong");
            return error;
        }
    }

    @Override
    public String buildRequestUrl(String city, String unit)
    {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(apiUrl)
                .path("/data/2.5/weather")
                .queryParam("q", city)
                .queryParam("units", unit)
                .queryParam("appid", apiKey)
                .build();
        return uriComponents.toUriString();
    }

    @Override
    public JsonObject mapResponseToReport(String response)
    {
        JsonObject report = new JsonObject();
        try {
            JsonObject wind = JsonParser.parseString(response).getAsJsonObject().getAsJsonObject("wind");
            JsonObject main = JsonParser.parseString(response).getAsJsonObject().getAsJsonObject("main");
            if (wind == null || main == null) {
                JsonObject error = new JsonObject();
                error.addProperty("error", "something went wrong");
                return error;
            }
            report.add("wind_speed", wind.get("speed"));
            report.add("temperature_degrees", main.get("temp"));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "something went wrong");
            return error;
        }
        return report;
    }

    @Override
    public Boolean isResponseValid(ResponseEntity<String> response) {
        if (response.getStatusCode().isError() || response.getBody() == null) {
            return false;
        }

        String statusCode = JsonParser.parseString(response.getBody()).
                getAsJsonObject()
                .get("cod")
                .getAsString();

        if (!statusCode.equals("200")) {
            return false;
        }

        return true;
    }
}
