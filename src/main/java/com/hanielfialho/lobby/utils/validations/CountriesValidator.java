package com.hanielfialho.lobby.utils.validations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountriesValidator {

    private static final List<String> validCountries = Arrays.asList(
            "Brazil", "Brasil", "United States", "Canada", "United Kingdom", "Australia", "France", "Germany", "Japan");

    public static boolean isValidCountry(String country) {
        return validCountries.contains(country);
    }
}
