package com.nukedemo.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nukedemo.core.services.nominatim.client.NominatimApiClient;
import com.nukedemo.core.services.overpass.client.OverpassApiClient;
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
import java.util.Map;

import static com.nukedemo.core.services.nominatim.client.NominatimApiClient.FEATURE_TYPE_COUNTRY;
import static com.nukedemo.core.services.nominatim.client.NominatimApiClient.FORMAT;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class CoreApplicationTests {

      private String overpassQuery1 = "[out:json][timeout:25];(node[\"amenity\"=\"post_box\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427););out body;>;out skel qt;";
      private String overpassQuery2 = "[out:json][timeout:30]; (node[\"amenity\"=\"parking\"][\"access\"!=\"private\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427);<;); out body center qt 100;";
      private String overpassQuery3 = "[out:json][timeout:180]; area(id:3601428125)->.searchArea; (way[\"landuse\"=\"military\"](area.searchArea);<;);  out body center qt 100;";
      private String overpassQuery4 = "[out:json][timeout:180]; area(id:3601428125)->.searchArea; (way[\"military\"~\"barracks|office|danger_area|training_area|airfield|base|naval_base\"](area.searchArea); relation[\"military\"~\"barracks|office|danger_area|training_area|airfield|base|naval_base\"](area.searchArea);); out body;>;out skel qt;";

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    OverpassApiClient overpassClient;

    @Autowired
    NominatimApiClient nominatimClient;

    @Test
    public void testOverpassClient() throws JsonProcessingException {
        log.info(overpassQuery4);
        String res = overpassClient.interpret(overpassQuery3);
        log.info(res);
        mapper.readValue(res, FeatureCollection.class);
    }

    @Test
    public void testNominatimClient() {
        String res = nominatimClient.search(FORMAT, FEATURE_TYPE_COUNTRY, "Canada");
        log.info(res);
    }

    @Test
    public void testOverpassUnmarshalling() {
        FeatureCollection featureCollection = deserializeFileToGeoJson("file:../data/mil/usa.geojson");
        featureCollection.getFeatures().get(0).<Map>getProperty("geocoding").get("osm_id");
    }

    @Test
    public void testNominatimUnmarshalling() {
        FeatureCollection featureCollection = deserializeFileToGeoJson("file:../data/country/canada.geojson");
        featureCollection.getFeatures().get(0).<Map>getProperty("geocoding").get("osm_id");
    }


    public FeatureCollection deserializeFileToGeoJson(String location) {
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
