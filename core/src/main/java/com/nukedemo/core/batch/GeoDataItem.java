package com.nukedemo.core.batch;

import lombok.Data;

import java.util.UUID;

@Data
public class GeoDataItem {

    private String country;

    public GeoDataItem(String country) {
        this.country = country;
    }

}
