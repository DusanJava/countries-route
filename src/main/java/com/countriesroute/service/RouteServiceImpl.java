package com.countriesroute.service;

import com.countriesroute.client.CountryClient;
import com.countriesroute.data.Country;
import com.countriesroute.exception.NoPathException;
import com.countriesroute.response.RouteResponse;
import com.countriesroute.util.MyCollectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RouteServiceImpl implements RouteService {

    private final CountryClient countryClient;
    private Map<Country, Boolean> visited = new HashMap<>();
    private Map<Country, Country> previous = new HashMap<>();

    @Override
    public RouteResponse getRoute(final String origin, final String destination) {

        final Map<String, Country> countries = countryClient.getCountries()
                                                            .stream()
                                                            .collect(Collectors.toMap(Country::getName,
                                                                                       Function.identity()));
        Country originCountry = Optional.ofNullable(countries.get(origin))
                                    .orElseThrow(() -> new NoPathException(String.format("Unknown origin country %s", origin)));
        Country destinationCountry = Optional.ofNullable(countries.get(destination))
                                         .orElseThrow(() -> new NoPathException(String.format("Unknown destination country %s", destination)));

        if (!originCountry.getRegion().connectedWith(destinationCountry.getRegion())) {
            throw new NoPathException(String.format(
                    "%s (%s) is not connected with %s (%s) by land",
                    originCountry.getRegion(), origin,
                    destinationCountry.getRegion(), destination));
        }

        RouteResponse response = new RouteResponse();
        response.setRoute(getPaths(countries, originCountry, destinationCountry));
        return response;
    }

    private List<String> getPaths(final Map<String, Country> countries, final Country originCountry, final Country destinationCountry) {

        Country currentCountry = originCountry;

        Queue<Country> pivot = new ArrayDeque<>();
        pivot.add(currentCountry);

        visited.put(currentCountry, true);

         while (!pivot.isEmpty()) {
            currentCountry = pivot.remove();
            log.debug("Visiting " + currentCountry.getName());
            if (currentCountry.equals(destinationCountry)) {
                log.debug("Origin and destination are equal");
                break;
            } else {
                for (String neighbour : currentCountry.getBorders()) {
                    Country neighbourCountry = countries.get(neighbour);
                    if(!visited.containsKey(neighbourCountry)){
                        log.debug("... registering neighbour " + neighbourCountry.getName());
                        pivot.add(neighbourCountry);
                        visited.put(neighbourCountry, true);
                        previous.put(neighbourCountry, currentCountry);
                        if (neighbourCountry.equals(destinationCountry)) {
                            log.debug("Shortest path found");
                            currentCountry = neighbourCountry;
                            break;
                        }
                    } else {
                        log.debug("... skipping neighbour " + neighbourCountry.getName());
                    }
                }
            }
        }

        if (!currentCountry.equals(destinationCountry)){
            throw new NoPathException("Cannot reach the path.");
        }

        List<Country> path = new ArrayList<>();
        for (Country node = destinationCountry; node != null; node = previous.get(node)) {
            path.add(node);
        }
        visited.clear();
        previous.clear();

        return path.stream()
                   .map(Country::getName)
                   .collect(MyCollectors.reversing());
    }
}
