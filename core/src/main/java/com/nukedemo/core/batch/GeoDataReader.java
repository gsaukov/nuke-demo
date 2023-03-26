package com.nukedemo.core.batch;

import org.springframework.batch.item.ItemReader;


public class GeoDataReader implements ItemReader<String> {

    private String country;

    public GeoDataReader(String country) {
        this.country = country;
    }

    @Override
    public String read() {
        return null;
    }
}
