package com.nukedemo.core.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

@Slf4j
public class GeoDataProcessor implements ItemProcessor<String, GeoDataItem> {

    private UUID uuid = UUID.randomUUID();
    @Override
    public GeoDataItem process(String item) {
        log.info("Processor ID: " + uuid + " Item: " + item);
        //process/Convert GeorJson items from nominatim
        return new GeoDataItem(item);
    }
}
