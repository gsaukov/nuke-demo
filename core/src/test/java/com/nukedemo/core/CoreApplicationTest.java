package com.nukedemo.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.nukedemo.core.services.clients.nominatim.NominatimApiClient;
import com.nukedemo.core.services.clients.overpass.OverpassApiClient;
import com.nukedemo.geocalculator.services.OsmToGeoJsonService;
import lombok.extern.slf4j.Slf4j;
import org.geojson.*;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.nukedemo.core.services.clients.nominatim.NominatimApiClient.FEATURE_TYPE_COUNTRY;
import static com.nukedemo.core.services.clients.nominatim.NominatimApiClient.FORMAT;

@SpringBootTest(classes = CoreApplication.class)
@Slf4j
@StepScope
@SpringBatchTest
class CoreApplicationTest {

      private String overpassQuery1 = "[out:json][timeout:25];(node[\"amenity\"=\"post_box\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427););out body;>;out skel qt;";
      private String overpassQuery2 = "[out:json][timeout:30]; (node[\"amenity\"=\"parking\"][\"access\"!=\"private\"](47.48047027491862,19.039797484874725,47.51331674014172,19.07404761761427);<;); out body center qt 100;";
      private String overpassQuery3 = "[out:json][timeout:180]; area(id:29879602)->.searchArea; (way[\"landuse\"=\"military\"](area.searchArea);<;);  out body center qt 100;";
      private String overpassQuery4 = "[out:json][timeout:180]; area(id:3600014296)->.searchArea; (way[\"military\"~\"barracks|office|danger_area|training_area|airfield|base|naval_base\"](area.searchArea); relation[\"military\"~\"barracks|office|danger_area|training_area|airfield|base|naval_base\"](area.searchArea);); out body;>;out skel qt;";

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    OverpassApiClient overpassClient;

    @Autowired
    NominatimApiClient nominatimClient;

    @Autowired
    OsmToGeoJsonService osmToGeoJsonService;

    @Test
    public void testOverpassClient() throws Exception {
        log.info(overpassQuery4);
        String res = overpassClient.interpret(overpassQuery4);
        log.info(res);
        log.info(osmToGeoJsonService.convert(res).toString());
    }

    @Test
    public void testNominatimClient() {
        String res = nominatimClient.search(FORMAT, FEATURE_TYPE_COUNTRY, "Canada");
        log.info(res);
    }

    @Test
    public void testOverpassUnmarshalling() {
        FeatureCollection featureCollection = deserializeFileToGeoJson("file:../data/mil/usa.geojson");
        featureCollection.getFeatures().get(0).getProperty("name");
    }

    @Test
    public void testNominatimUnmarshalling() {
        FeatureCollection featureCollection = deserializeFileToGeoJson("file:../data/mil/canada_point.geojson");
        featureCollection.getFeatures().get(0).<Map>getProperty("geocoding").get("osm_id");
    }

    @Test
    public void testCenterCalculations() {
        FeatureCollection featureCollection = deserializeFileToGeoJson("file:../data/mil/usa.geojson");
        for (Feature f : featureCollection.getFeatures()) {
            GeoJsonObject g = f.getGeometry();
            if (g instanceof FeatureCollection || g instanceof Feature || g instanceof MultiLineString) {
                System.out.println("");
            }
//            List<com.esri.core.geometry.Point> points = new LinkedList<>();
//            points.add(new com.esri.core.geometry.Point(11.560385, 48.176874));
//            points.add(new com.esri.core.geometry.Point(11.560551, 48.177206));
//            points.add(new com.esri.core.geometry.Point(11.559370, 48.176901));
//            points.add(new com.esri.core.geometry.Point(11.557062, 48.176892));
//            points.add(new com.esri.core.geometry.Point(11.558105, 48.177123));
//            points.add(new com.esri.core.geometry.Point(11.557182, 48.177492));
//            Set<List<com.esri.core.geometry.Point>> results = DBSCAN.cluster(points, 75, 1);
        }
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
