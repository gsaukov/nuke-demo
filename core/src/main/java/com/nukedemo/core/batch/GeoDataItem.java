package com.nukedemo.core.batch;

import lombok.Data;

import java.util.UUID;

@Data
public class GeoDataItem {

    private String countryName;

    private String countryOsm;

    private String countryGeoJson;

    public GeoDataItem(String countryName) {
        this.countryName = countryName;
    }

}
