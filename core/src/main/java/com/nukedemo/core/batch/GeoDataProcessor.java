package com.nukedemo.core.batch;

import org.springframework.batch.item.ItemProcessor;

public class GeoDataProcessor implements ItemProcessor<String, GeoDataItem> {

    @Override
    public GeoDataItem process(String item) {
        return null;
    }
}
