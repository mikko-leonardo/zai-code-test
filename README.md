A Spring Boot application that provides weather information through a REST API, with support for multiple weather data providers.

## Features

- RESTful API for basic weather information.
- WeatherStack as a primary provider with OpenWeather as a failover.
- Response Caching using Google Guava

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- API keys for OpenWeather and WeatherStack

    - For ease of demonstration, API keys are provided.

## Technologies

- Spring Boot 3.4.5
- Google Gson 2.13.1
- Google Guava 33.4.8-jre
- Lombok