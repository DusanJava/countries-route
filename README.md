# Backend Developer Test

## Table of contents
- [Task](#task)
- [Solution](#solution)
- [How to run](#how-to-run)

## Task
Your task is to create a simple Spring Boot service, that is able to calculate any possible land
route from one country to another. The objective is to take a list of country data in JSON format
and calculate the route by utilizing individual countries border information.

Specifications:
● Spring Boot, Maven
● Data link: [endpoint](https://raw.githubusercontent.com/mledoze/countries/master/countries.json) 
● The application exposes REST endpoint /routing/{origin}/{destination} that
returns a list of border crossings to get from origin to destination
● Single route is returned if the journey is possible
● Algorithm needs to be efficient
● If there is no land crossing, the endpoint returns HTTP 400
● Countries are identified by cca3 field in country data
● HTTP request sample (land route from Czech Republic to Italy):
    ○ GET /routing/CZE/ITA HTTP/1.0 :
        {
        "route": ["CZE", "AUT", "ITA"]
        }

Expected deliveries:
1. Source code
2. Instructions on how to build and run the application

_________

## Solution

I have created a simple Spring Boot application exposing a REST endpoint with a logic of routing between countries. 
A list of countries with borders is available at another endpoint. So the application serves both as a REST server and REST client.
I also created:
- Exception handler for different cases,
- Tests to cover "Happy path" and exceptions,
- Mapper with Mapstruct

### How to run

The only prerequisite is Java 11.

 - Build the application:
   - Windows: `mvnw.cmd clean install`
   - Unix: `mvnw clean install`
   
 - Run the application:
   - Windows: `mvnw.cmd spring-boot:run`
   - Unix: `mvnw spring-boot:run`

 - Try it out in Postman / Command line:
    ```
   curl --request GET \
    --url http://localhost:8080/routing/CZE/ITA
   ```


