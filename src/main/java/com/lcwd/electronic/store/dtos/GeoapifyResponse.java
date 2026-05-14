package com.lcwd.electronic.store.dtos;




import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GeoapifyResponse {

    private List<Feature> features;

    @Setter
    @Getter
    public static class Feature {

        private Properties properties;
    }

    @Setter
    @Getter
    public static class Properties {

        private String formatted;
    }
}
