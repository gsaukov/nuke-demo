package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeoCalculatorApp {

    private static final String PROPERTY_PREFIX = "ND:";

    public String calculate (String geojson) {
        FeatureCollection featureCollection = FeatureCollection.fromJson(geojson);
        for(Feature feature : featureCollection.features()) {
            preCalculate(feature);
        }
        return featureCollection.toJson();
    }

    private void preCalculate(Feature feature) {
        Geometry g = feature.geometry();
        Point center = (Point)(TurfMeasurement.center(feature).geometry());
        addProperty(feature, "center", center.toJson());
        addProperty(feature, "area", String.valueOf(TurfMeasurement.area(feature)));
    }

    private static void addProperty(Feature feature, String name, String value) {
        feature.addStringProperty(PROPERTY_PREFIX + name, value);

    }


}
