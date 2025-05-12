package com.zai.zaicodetest.reportcache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CacheStoreBeans {

    @Bean
    public CacheStore<WeatherReport> weatherReportCache() {
        return new CacheStore<WeatherReport>();
    }

}
