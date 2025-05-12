package com.zai.zaicodetest.reportcache;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherReport {

    private JsonObject report;

    private Long timestamp;

}
