package com.countriesroute.client;

import com.countriesroute.data.Country;
import com.countriesroute.mapper.CountryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class CountryClient {

    private final String countryUrl;
    private final RestTemplate restTemplate;
    private final CountryMapper countryMapper;

    public CountryClient(@Value("${rest.config.countries.url}") String countryUrl,
                         final RestTemplate restTemplate, final CountryMapper countryMapper) {
        this.countryUrl = countryUrl;
        this.restTemplate = restTemplate;
        this.countryMapper = countryMapper;
    }

    public List<Country> getCountries() {
        log.debug("getCountries() calling country service countryUrl: {}" , countryUrl);
        try {
            ResponseEntity<CountryDto[]> response = restTemplate.getForEntity(countryUrl, CountryDto[].class);
            return countryMapper.fromDto(Arrays.asList(Objects.requireNonNull(response.getBody())));
        } catch (HttpServerErrorException e) {
            log.error("Cannot retrieve countries from country service, because of server error", e);
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            log.error("Cannot retrieve countries from country service, because of client error", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }
}
