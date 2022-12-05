package com.countriesroute.service;

import com.countriesroute.response.RouteResponse;

public interface RouteService {

    RouteResponse getRoute(String origin, String destination);
}
