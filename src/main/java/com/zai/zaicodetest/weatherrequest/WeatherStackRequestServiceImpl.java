package com.zai.zaicodetest.weatherrequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
class WeatherStackRequestServiceImpl implements WeatherRequestService
{
    @Value("${weatherstack.apiKey}")
    private String apiKey;

    @Value("${weatherstack.apiUrl}")
    private String apiUrl;

    @Override
    public String sendWeatherReportRequest(String city, String unit)
    {
        String uri = buildRequestUrl(city, unit);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        if(response.getStatusCode().isError() || response.getBody() == null)
        {
            return "Weather Stack API request failed";
        }

        return mapResponseToReport(response.getBody());

    }

    @Override
    public String buildRequestUrl(String city, String unit)
    {
        char weatherStackUnitCode = 'm';

        if (unit.equals("imperial")) {
            weatherStackUnitCode = 'f';
        } else if (unit.equals("scientific")) {
            weatherStackUnitCode = 's';
        }

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(apiUrl)
                .path("/current")
                .queryParam("access_key", apiKey)
                .queryParam("query", city)
                .queryParam("units", weatherStackUnitCode)
                .build();

        return uriComponents.toUriString();
    }

    @Override
    public String mapResponseToReport(String response)
    {
        JsonObject report = new JsonObject();
        try {
            JsonObject current = JsonParser.parseString(response).getAsJsonObject().getAsJsonObject("current");
            report.add("wind_speed", current.get("wind_speed"));
            report.add("temperature_degrees", current.get("temperature"));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return "Weather Stack API request failed";
        }

        return report.toString();
    }
}
