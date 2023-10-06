package com.nukedemo.core.batch;

import com.mapbox.geojson.FeatureCollection;

import com.nukedemo.geocalculator.services.GeoCalculatorApp;
import com.nukedemo.geocalculator.services.OsmToGeoJsonService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@StepScope
public class GeoDataProcessor implements ItemProcessor<GeoDataItem, GeoDataItem> {

    private UUID uuid = UUID.randomUUID();

    @Autowired
    private OsmToGeoJsonService osmToGeoJsonService;

    @Autowired
    private GeoCalculatorApp geoCalculatorApp;

    public GeoDataProcessor() {

    }

    @Override
    public GeoDataItem process(GeoDataItem item) throws Exception {
//        log.info("Processor ID: " + uuid + " Item: " + item);

        String features = osmToGeoJsonService.convert(item.getCountryOsm());
        item.setCountryGeoJson(geoCalculatorApp.calculate(features));
        return item;
    }
}
