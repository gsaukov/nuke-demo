package com.nukedemo.dbscan;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;

public class JTSFunctionsTest {

    @Test
    public void JTSTestAreaCalcultation() {
        Coordinate[] coordinates = new Coordinate[] {
                new Coordinate(0, 0),
                new Coordinate(10, 0),
                new Coordinate(10, 10),
                new Coordinate(0, 10),
                new Coordinate(0, 0)
        };

        // Create a LinearRing from the array of coordinates
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);

        // Create a Polygon from the LinearRing
        Polygon polygon = geometryFactory.createPolygon(linearRing, null);

        // Calculate the area of the polygon
        double area = polygon.getArea();

        System.out.println("Polygon area: " + area);
    }

    @Test
    public void JTSAreaCalculationFromGEOJson () throws ParseException {
        String geoJsonStr = "{\"type\": \"Feature\", \"properties\": {}, \"geometry\": {\"type\": \"Polygon\", \"coordinates\": [[[0, 0], [0, 10], [10, 10], [10, 0], [0, 0]]]}}";

        // Convert GeoJSON feature to JTS Polygon object
        GeoJsonReader reader = new GeoJsonReader();
        Geometry geometry = reader.read(geoJsonStr);
        Polygon polygon = (Polygon) geometry;

        // Calculate polygon area
        double area = polygon.getArea();

        System.out.println("Polygon area: " + area);
    }

}