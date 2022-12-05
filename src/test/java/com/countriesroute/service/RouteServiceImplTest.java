package com.countriesroute.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.countriesroute.client.CountryClient;
import com.countriesroute.data.Country;
import com.countriesroute.data.Region;
import com.countriesroute.exception.NoPathException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest
class RouteServiceImplTest {

    @Autowired
    private RouteService routeService;

    @MockBean
    private CountryClient countryClient;

    @BeforeEach
    private void beforeEach() {
        Mockito.clearInvocations(countryClient);
    }

    @Test
    void testUnknownOrigin() {
        Mockito.when(countryClient.getCountries()).thenReturn(List.of(
                new Country("CZE", Region.EUROPE, List.of("HUN", "SLV")),
                new Country("AUT", Region.EUROPE, List.of("LIE", "DEU")),
                new Country("ITA", Region.EUROPE, List.of("SWI", "FRA"))));

        NoPathException exception = assertThrows(NoPathException.class, () -> routeService.getRoute("ABC", "SWI"));
        assertEquals("Unknown origin country ABC", exception.getMessage());
    }

    @Test
    void testUnknownDestination() {
        Mockito.when(countryClient.getCountries()).thenReturn(List.of(
                new Country("CZE", Region.EUROPE, List.of("HUN", "SLV")),
                new Country("AUT", Region.EUROPE, List.of("LIE", "DEU")),
                new Country("ITA", Region.EUROPE, List.of("SWI", "FRA"))));

        NoPathException exception = assertThrows(NoPathException.class, () -> routeService.getRoute("ITA", "ABC"));
        assertEquals("Unknown destination country ABC", exception.getMessage());
    }

    @Test
    void testNonContinentalRoute() {
        Mockito.when(countryClient.getCountries()).thenReturn(List.of(
                new Country("KNA", Region.AMERICAS, List.of()),
                new Country("AUT", Region.EUROPE, List.of())));

        NoPathException exception = assertThrows(NoPathException.class, () -> routeService.getRoute("KNA", "AUT"));
        assertEquals("AMERICAS (KNA) is not connected with EUROPE (AUT) by land", exception.getMessage());
    }

    @Test
    void testRoute() {
        Mockito.when(countryClient.getCountries()).thenReturn(List.of(
                new Country("CZE", Region.EUROPE, List.of("AUT")),
                new Country("AUT", Region.EUROPE, List.of("CZE", "ITA")),
                new Country("ITA", Region.EUROPE, List.of("AUT", "FRA")),
                new Country("FRA", Region.EUROPE, List.of("ITA"))));

        assertEquals(List.of("CZE", "AUT", "ITA", "FRA"), routeService.getRoute("CZE", "FRA").getRoute());
    }
}
