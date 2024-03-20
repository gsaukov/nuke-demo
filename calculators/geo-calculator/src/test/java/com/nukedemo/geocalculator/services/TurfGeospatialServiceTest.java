package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class TurfGeospatialServiceTest {

    TurfGeospatialService service;

    @BeforeEach
    void setUp() throws ScriptException, IOException {
        service = new TurfGeospatialService(new GraalVMJSScriptingEngineService());
    }

    @Test
    void unionSimple() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_simple.json");
        Geometry geometry = service.union(featureCollection.features());
        log.info(geometry.toJson());
    }

    @Test
    void unionAdvanced() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_double.json");
        Geometry geometry = service.union(featureCollection.features());
        log.info(geometry.toJson());
    }

    FeatureCollection getFeatureCollection(String fileName) throws IOException {
        Path path = new PathMatchingResourcePatternResolver().getResource(fileName).getFile().toPath();
        String geoJson = Files.readString(path, StandardCharsets.UTF_8);
        return FeatureCollection.fromJson(geoJson);
    }
}