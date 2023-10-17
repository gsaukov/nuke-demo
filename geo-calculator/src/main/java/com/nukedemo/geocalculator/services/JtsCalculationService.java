package com.nukedemo.geocalculator.services;

import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JtsCalculationService {

    //longitude values are considered the x-coordinate, while latitude values are the y-coordinate
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public com.mapbox.geojson.Geometry union(List<com.mapbox.geojson.Feature> features) throws Exception {
        List<Geometry> polygons = getJtSPolygons(features);
        if (polygons.isEmpty()) {
            return null;
        }
        MultiPolygon cumulative = toMultiPolygon(polygons.get(0));
        if (polygons.size() < 2) {
            return toTurfGeometry(cumulative);
        }
        for (int i = 1; i < polygons.size(); i++) {//skip first
            cumulative.union(polygons.get(i));
        }
        return toTurfGeometry(cumulative);
    }

    private MultiPolygon toMultiPolygon(Geometry geometry) {
        if (geometry instanceof MultiPolygon) {
            return (MultiPolygon) geometry;
        } else {
            return new MultiPolygon(new Polygon[]{(Polygon) geometry}, geometryFactory);
        }
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

    private MultiPolygon toJtsMultyPolygon(com.mapbox.geojson.Feature feature) {
        com.mapbox.geojson.MultiPolygon turfMultiPolygon = (com.mapbox.geojson.MultiPolygon) (feature.geometry());
        List<Polygon> polygons = new ArrayList<>();
        for (com.mapbox.geojson.Polygon polygon : turfMultiPolygon.polygons()) {
            polygons.add(toJtsPolygon(polygon));
        }
        return new MultiPolygon(polygons.toArray(new Polygon[0]), new GeometryFactory());
    }


    public Polygon toJtsPolygon(com.mapbox.geojson.Feature feature) {
        return toJtsPolygon((com.mapbox.geojson.Polygon) (feature.geometry()));
    }

    public Polygon toJtsPolygon(com.mapbox.geojson.Polygon turfPolygon) {
        List<com.mapbox.geojson.Point> exteriorRings = turfPolygon.coordinates().get(0);//only exterior ring is used holes are omitted
        List<Coordinate> coordinates = new ArrayList<>();
        for (com.mapbox.geojson.Point point : exteriorRings) {
            coordinates.add(new Coordinate(point.longitude(), point.latitude(), point.altitude()));
        }

        // Create a LinearRing from the array of coordinates
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));

        // Create a Polygon from the LinearRing
        return geometryFactory.createPolygon(linearRing, null);
    }


    public com.mapbox.geojson.Geometry toTurfGeometry(Geometry geometry) {
        Coordinate[] coordinates;
        if (Geometry.TYPENAME_MULTIPOLYGON.equals(geometry.getGeometryType())) {
            return toTurfMultiPolygon((MultiPolygon) geometry);
        } else {
            return toTurfPolygon((Polygon) geometry);
        }
    }

    public com.mapbox.geojson.MultiPolygon toTurfMultiPolygon(MultiPolygon multiPolygon) {
        List<Geometry> geometries = new ArrayList<>();
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            geometries.add(multiPolygon.getGeometryN(i));
        }
        List<com.mapbox.geojson.Polygon> polygons = new ArrayList<>();
        for (Geometry geometry : geometries) {
            polygons.add(toTurfPolygon((Polygon) geometry));
        }
        return com.mapbox.geojson.MultiPolygon.fromPolygons(polygons);
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
