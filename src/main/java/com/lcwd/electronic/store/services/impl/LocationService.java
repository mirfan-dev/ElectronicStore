

package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.GeoapifyResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_KEY =
            "f482d871a3904277bb7cbc087d92c718";

    public String getCurrentLocation(
            double lat,
            double lon) {

        String url =
                "https://api.geoapify.com/v1/geocode/reverse?"
                        + "lat=" + lat
                        + "&lon=" + lon
                        + "&apiKey=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        ResponseEntity<GeoapifyResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        GeoapifyResponse.class
                );

        GeoapifyResponse body = response.getBody();

        if (body != null
                && body.getFeatures() != null
                && !body.getFeatures().isEmpty()) {

            return body.getFeatures()
                    .get(0)
                    .getProperties()
                    .getFormatted();
        }

        return "Location not found";
    }
}