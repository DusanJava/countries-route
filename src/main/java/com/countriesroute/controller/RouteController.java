package com.countriesroute.controller;

import com.countriesroute.response.RouteResponse;
import com.countriesroute.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping(value = "/routing/{origin}/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteResponse> getRoute(@PathVariable String origin, @PathVariable String destination) {
        final RouteResponse route = routeService.getRoute(origin, destination);
        return ResponseEntity.ok(route);
    }
}
