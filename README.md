A Spring Boot application that provides weather information through a REST API, with support for multiple weather data providers.

## Features

- RESTful API for basic weather information.
- WeatherStack as a primary provider with OpenWeather as a failover.
- Response Caching using Google Guava

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- API keys for OpenWeather and WeatherStack

## Technologies

- Spring Boot 3.4.5
- Google Gson 2.13.1
- Google Guava 33.4.8-jre
- Lombok

## Instructions for building the project:
- Clone the repository
- run `mvn clean package` in project root directory
- run `java -jar zai-code-test-0.0.1-SNAPSHOT.jar` in directory named "target"
- run `curl http://localhost:8080/v1/weather?city=melbourne` to test

    - if there is no city parameter in the request, it will still return melbourne data.

## Using the included zip file
- Download and extract "weatherreport.zip"
- run `java -jar zai-code-test-0.0.1-SNAPSHOT.jar` in directory named "target"
- run `curl http://localhost:8080/v1/weather?city=melbourne` to test

  - if there is no city parameter in the request, it will still return melbourne data.

## Things I Would have done differently
- keys, url, and other configurations would have been handled via environment variables
- configurable cache properties
- refreshAfterWrite cache approach instead of "brute forcing" cache implementation
- better error responses and exception handling
- better logging
- Swagger/OpenAPI documentation
- Cache unit tests
- Health/status checks for external weather APIs
- fix malformed json exception on WeatherStackRequestServiceImplTest
- input validation
- better response messages (proper DTO instead of Strings)