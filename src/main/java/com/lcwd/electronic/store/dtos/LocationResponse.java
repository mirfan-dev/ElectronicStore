package com.lcwd.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {

    private double lon;
    private double lat;
    private String display_name;
}
