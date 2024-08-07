package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
class JtsCalculationServiceTest {

    private static String GEOJSON;

    private static FeatureCollection FEATURE_COLLECTION;

    JtsCalculationService service;

    @BeforeAll
    static void beforeAll() throws IOException {
        Path path = new PathMatchingResourcePatternResolver().getResource("union_hole_double.json").getFile().toPath();
        GEOJSON = Files.readString(path, StandardCharsets.UTF_8);
        FEATURE_COLLECTION = FeatureCollection.fromJson(GEOJSON);
    }

    @BeforeEach
    void setUp() {
        service = new JtsCalculationService();
    }

    @Test
    void union() throws Exception {
        Geometry geometry = service.union(FEATURE_COLLECTION.features());
        log.info(geometry.toJson());
    }
}