package com.nukedemo.dbscanturf;

import com.google.gson.JsonElement;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

public class MapboxTurfFunctionsTest {
    @Test
    public void testMapBoxTurf() {
        FeatureCollection f =  FeatureCollection.fromJson(param);
        double area = TurfMeasurement.area(f);
    }

    @Test
    public void testCenterCalculations() throws IOException, URISyntaxException, ParseException {
        String fileContent = new String(Files.readAllBytes(Paths.get("../data/mil/usa.geojson")));
        FeatureCollection fc = FeatureCollection.fromJson(fileContent);
        System.out.println(TurfMeasurement.area(fc));
        Point sample = (Point)(TurfMeasurement.center(fc.features().get(0)).geometry());

        for (Feature f : fc.features()) {
            Point center = (Point)(TurfMeasurement.center(f).geometry());
            System.out.print(center.toJson());
            System.out.print("|| area ");
            System.out.print(TurfMeasurement.area(f));
            System.out.print("|| distance ");
            System.out.print(TurfMeasurement.distance(center, sample));
            System.out.println(" ");
        }
    }

    private String param = "{\"type\":\"FeatureCollection\",\"generator\":\"overpass-ide\",\"copyright\":\"Thedataincludedinthisdocumentisfromwww.openstreetmap.org.ThedataismadeavailableunderODbL.\",\"timestamp\":\"2023-02-24T17:46:38Z\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"@id\":\"way/612169247\",\"building\":\"yes\",\"military\":\"barracks\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-104.775668,38.7362686],[-104.7756934,38.7362871],[-104.7758194,38.7361817],[-104.7758,38.7361676],[-104.77588,38.7361007],[-104.775854,38.7360818],[-104.7758628,38.7360745],[-104.775834,38.7360536],[-104.7758534,38.7360374],[-104.7758141,38.7360088],[-104.7757905,38.7360286],[-104.7757603,38.7360066],[-104.7756793,38.7360745],[-104.7757058,38.7360938],[-104.7756996,38.736099],[-104.7757226,38.7361157],[-104.7756787,38.7361525],[-104.7756499,38.7361315],[-104.7755699,38.7361984],[-104.7755979,38.7362188],[-104.7755698,38.7362424],[-104.7756391,38.7362927],[-104.775668,38.7362686]]]},\"id\":\"way/612169247\"}]}";
}