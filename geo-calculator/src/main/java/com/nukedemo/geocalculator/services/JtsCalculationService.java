package com.nukedemo.geocalculator.services;

import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JtsCalculationService {

    public com.mapbox.geojson.Geometry union(List<com.mapbox.geojson.Feature> features) throws Exception {
        List<Geometry> polygons = getJtSPolygons(features);
        if (polygons.isEmpty()) {
            return null;
        }
        if (polygons.size() < 2) {
            return toTurfGeometry(polygons.get(0));
        }
        Geometry cumulative = polygons.get(0);

        for (int i = 1; i < polygons.size(); i++) {//skip first
            cumulative.union(polygons.get(i));
        }

        return null;
    }

    private List<Geometry> getJtSPolygons(List<com.mapbox.geojson.Feature> features) {
        List<Geometry> polygons = features.stream()
                .filter(f -> (f.geometry() instanceof com.mapbox.geojson.Polygon))
                .map(f -> toJtsPolygon(f)).collect(Collectors.toList());

        List<Geometry> multiPolygons = features.stream()
                .filter(f -> (f.geometry() instanceof com.mapbox.geojson.MultiPolygon))
                .map(f -> toJtsMultyPolygon(f)).collect(Collectors.toList());

        polygons.addAll(multiPolygons);
        return polygons;
    }

    private MultiPolygon toJtsMultyPolygon(com.mapbox.geojson.Feature f) {
        return null;
    }


    public Polygon toJtsPolygon(com.mapbox.geojson.Feature feature) {
        com.mapbox.geojson.Polygon turfPolygon = (com.mapbox.geojson.Polygon) (feature.geometry());
        List<com.mapbox.geojson.Point> exteriorRings = turfPolygon.coordinates().get(0);//only exterior ring is used holes are omitted
        List<Coordinate> coordinates = new ArrayList<>();
        for (com.mapbox.geojson.Point point : exteriorRings) {
            coordinates.add(new Coordinate(point.latitude(), point.latitude(), point.altitude()));
        }

        // Create a LinearRing from the array of coordinates
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));

        // Create a Polygon from the LinearRing
        Polygon polygon = geometryFactory.createPolygon(linearRing, null);
        return polygon;
    }


    public com.mapbox.geojson.Geometry toTurfGeometry(Geometry geometry) {
        Coordinate[] coordinates;
        if (Geometry.TYPENAME_MULTIPOLYGON.equals(geometry.getGeometryType())) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            List<Geometry> geometries = new ArrayList<>();
            for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                geometries.add(multiPolygon.getGeometryN(i));
            }
//            geometries.get
            return null;
        } else {
            return toTurfPolygon((Polygon) geometry);
        }
    }

    public com.mapbox.geojson.Polygon toTurfPolygon(Polygon polygon) {
        Coordinate[] coordinates = polygon.getExteriorRing().getCoordinates();//only exterior ring is used holes are omitted
        List<com.mapbox.geojson.Point> turfPoints = toTurfPoints(coordinates);
        List<List<com.mapbox.geojson.Point>> exteriorPoints = new ArrayList<>();
        exteriorPoints.add(turfPoints);
        return com.mapbox.geojson.Polygon.fromLngLats(exteriorPoints);
    }

    public List<com.mapbox.geojson.Point> toTurfPoints(Coordinate[] coordinates) {
        List<com.mapbox.geojson.Point> turfPoints = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            turfPoints.add(toTurfPoint(coordinate));
        }
        return turfPoints;
    }

    public com.mapbox.geojson.Point toTurfPoint(Coordinate coordinate) {
        return com.mapbox.geojson.Point.fromLngLat(coordinate.getX(), coordinate.getY(), coordinate.getZ());
    }
}
