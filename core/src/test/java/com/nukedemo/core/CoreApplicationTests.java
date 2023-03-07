package com.nukedemo.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nukedemo.core.services.nominatim.client.NominatimApiClient;
import com.nukedemo.core.services.overpass.client.OverpassApiClient;
import com.nukedemo.core.services.overpass.model.OverpassResponse;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class CoreApplicationTests {

    String overpassQuery1 = "[out:json][timeout:25];(node[\"amenity\"=\"post_box\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427););out body;>;out skel qt;";
    String overpassQuery2 = "[out:json][timeout:30]; (node[\"amenity\"=\"parking\"][\"access\"!=\"private\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427);<;); out body center qt 100;";
    String overpassQuery3 = "[out:json][timeout:180]; area(id:3601428125)->.searchArea; (way[\"landuse\"=\"military\"](area.searchArea);<;); out body center qt 100;";

    @Autowired
    OverpassApiClient overpassClient;

    @Autowired
    NominatimApiClient nominatimClient;

    @Test
    public void testOverpassClient() {
        log.info(overpassQuery1);
        String res = overpassClient.interpret(overpassQuery1);
        log.info(res);
    }

    @Test
    public void testNominatimClient() {
        String res = nominatimClient.search("json", "Canada");
        log.info(res);
    }

    @Test
    public void testNominatimMarshalling() {
        deserializeFileToModel("file:../data/mil/usa.geojson");
        deserializeFileToGeoJson("file:../data/mil/usa.geojson");
    }

    public OverpassResponse deserializeFileToModel(String location) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        OverpassResponse sample = null;
        try {
            InputStream inJson = new PathMatchingResourcePatternResolver()
                    .getResource(location).getInputStream();
            sample = mapper.readValue(inJson, OverpassResponse.class);
        } catch (IOException e) {
            log.error("deserialization failed", e);
        }
        return sample;
    }

    public FeatureCollection deserializeFileToGeoJson(String location) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        FeatureCollection sample = null;
        try {
            InputStream inJson = new PathMatchingResourcePatternResolver()
                    .getResource(location).getInputStream();
            sample = mapper.readValue(inJson, FeatureCollection.class);
        } catch (IOException e) {
            log.error("deserialization failed", e);
        }
        return sample;
    }

}
