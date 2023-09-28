package com.nukedemo.geocalculator.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.geojson.*;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.turf.TurfMeasurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class GeoCalculatorApp {

    private static final String PROPERTY_PREFIX = "ND:";

    public String calculate(String geojson) throws IOException {
        FeatureCollection featureCollection = readJson(geojson);
        for (Feature feature : featureCollection.features()) {
            preCalculate(feature);
        }
        return featureCollection.toJson();
    }

    private void preCalculate(Feature feature) {
        Geometry g = feature.geometry();
        Point center = (Point) (TurfMeasurement.center(feature).geometry());
        addProperty(feature, "center", center.toJson());
        addProperty(feature, "area", String.valueOf(TurfMeasurement.area(feature)));
    }

    private static void addProperty(Feature feature, String name, String value) {
        feature.addStringProperty(PROPERTY_PREFIX + name, value);

    }

    private FeatureCollection readJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GeoJsonAdapterFactory.create())
                .registerTypeAdapterFactory(GeometryAdapterFactory.create())
                .setPrettyPrinting()
                .create();
        return gson.fromJson(json, FeatureCollection.class);
    }


}
