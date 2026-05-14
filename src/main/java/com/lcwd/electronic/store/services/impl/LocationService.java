package com.lcwd.electronic.store.services.impl;


import com.lcwd.electronic.store.dtos.LocationResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCurrentLocation(
            double lat,
            double lon) {

        String url =
                "https://nominatim.openstreetmap.org/reverse?lat="
                        + lat
                        + "&lon="
                        + lon
                        + "&format=json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootApp");

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        ResponseEntity<LocationResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        LocationResponse.class
                );

        return response.getBody().getDisplay_name();
    }
}