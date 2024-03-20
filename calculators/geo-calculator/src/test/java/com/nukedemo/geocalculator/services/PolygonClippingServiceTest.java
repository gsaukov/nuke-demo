package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PolygonClippingServiceTest {

    PolygonClippingService service;

    @BeforeEach
    void setUp() {
        service = new PolygonClippingService();
    }

    @Test
    void unionMergeSimple() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_simple.json");
        Geometry geometry = service.unionMerge(featureCollection.features());
        log.info("union merge simple: " + geometry.toJson());
    }

    @Test
    void unionMergeAdvanced() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_double.json");
        Geometry geometry = service.unionMerge(featureCollection.features());
        log.info("union merge advance: " + geometry.toJson());
    }

    @Test
    void unionSimple() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_simple.json");
        Geometry geometry = service.union(featureCollection.features());
        log.info("union simple: " + geometry.toJson());
    }

    @Test
    void unionAdvanced() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_double.json");
        Geometry geometry = service.union(featureCollection.features());
        log.info("union advanced: " + geometry.toJson());
    }


    FeatureCollection getFeatureCollection(String fileName) throws IOException {
        Path path = new PathMatchingResourcePatternResolver().getResource(fileName).getFile().toPath();
        String geoJson = Files.readString(path, StandardCharsets.UTF_8);
        return FeatureCollection.fromJson(geoJson);
    }
}