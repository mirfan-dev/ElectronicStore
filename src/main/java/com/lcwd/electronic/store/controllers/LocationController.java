package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.services.impl.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/current")
    public String currentLocation(
            @RequestParam double lat,
            @RequestParam double lon) {

        return locationService.getCurrentLocation(lat, lon);
    }
}