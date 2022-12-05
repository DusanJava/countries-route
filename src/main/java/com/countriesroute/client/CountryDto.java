package com.countriesroute.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    private String cca3;
    private String region;
    private List<String> borders;

}
