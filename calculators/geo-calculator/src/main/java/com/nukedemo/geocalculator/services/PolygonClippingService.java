package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.Feature;
import com.menecats.polybool.models.Polygon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Service
public class PolygonClippingService {

    public com.mapbox.geojson.Geometry union(List<Feature> features) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(16);

        List<List<List<List<double[]>>>> featuresCoordinates = forkJoinPool.invoke(new PolygonMerginRecursiveTask(extractFeaturesCoordinates(features), 2));
        if (featuresCoordinates.isEmpty()) {
            return null;
        }

        return toTurfGeometriesList(featuresCoordinates.get(0));
    }

    public List<List<List<List<double[]>>>> extractFeaturesCoordinates(List<Feature> features) {
        List<List<List<List<double[]>>>> polygons = new ArrayList<>();
        for (Feature feature : features) {
            polygons.add(extractFeatureCoordinates(feature));
        }
        return polygons;
    }

    public List<List<List<double[]>>> extractFeatureCoordinates(Feature feature) {
        List<List<List<double[]>>> coordinates = new ArrayList<>();
        com.mapbox.geojson.Geometry geometry = feature.geometry();

        if (geometry instanceof com.mapbox.geojson.Polygon) {
            coordinates.add(extractPolygonCoordinates((com.mapbox.geojson.Polygon) geometry));
        } else if (geometry instanceof com.mapbox.geojson.MultiPolygon) {
            com.mapbox.geojson.MultiPolygon multiPolygon = (com.mapbox.geojson.MultiPolygon) geometry;
            for (com.mapbox.geojson.Polygon polygon : multiPolygon.polygons()) {
                coordinates.add(extractPolygonCoordinates(polygon));
            }
        }

        return coordinates;
    }

    public List<List<double[]>> extractPolygonCoordinates(com.mapbox.geojson.Polygon polygon) {
        List<List<double[]>> polygonCoordinates = new ArrayList<>();
        for (List<com.mapbox.geojson.Point> region : polygon.coordinates()) {
            List<double[]> ringList = new ArrayList<>();
            for (com.mapbox.geojson.Point coordinate : region) {
                ringList.add(new double[]{coordinate.longitude(), coordinate.latitude()});
            }
            polygonCoordinates.add(ringList);
        }
        return polygonCoordinates;
    }

    public com.mapbox.geojson.Geometry toTurfGeometriesList(List<List<List<double[]>>> cumulatives) {
        List<com.mapbox.geojson.Polygon> multiPolygon = new ArrayList<>();
        for (List<List<double[]>> coordinates : cumulatives) {
            multiPolygon.add(com.mapbox.geojson.Polygon.fromLngLats(createPolygonPoints(coordinates)));
        }
        return com.mapbox.geojson.MultiPolygon.fromPolygons(multiPolygon);
    }

    public com.mapbox.geojson.Geometry toTurfGeometries(List<Polygon> cumulatives) {
        List<com.mapbox.geojson.Polygon> multiPolygon = new ArrayList<>();
        for (Polygon cumulative : cumulatives) {
            List<List<double[]>> coordinates = cumulative.getRegions();
            multiPolygon.add(com.mapbox.geojson.Polygon.fromLngLats(createPolygonPoints(coordinates)));
        }
        return com.mapbox.geojson.MultiPolygon.fromPolygons(multiPolygon);
    }

    public com.mapbox.geojson.Geometry toTurfGeometry(List<List<double[]>> coordinates) {
        List<List<com.mapbox.geojson.Point>> polygonPoints = createPolygonPoints(coordinates);
        return com.mapbox.geojson.MultiPolygon.fromPolygons(Arrays.asList(com.mapbox.geojson.Polygon.fromLngLats(polygonPoints)));
    }

    public List<List<com.mapbox.geojson.Point>> createPolygonPoints(List<List<double[]>> coordinates) {
        List<List<com.mapbox.geojson.Point>> polygonPoints = new ArrayList<>();
        // backward loop fixes polygon outer ring position that should be first, but it comes last.
        for (int i = coordinates.size() - 1; i >= 0; i--) {
            List<double[]> polygon = coordinates.get(i);
            List<com.mapbox.geojson.Point> points = new ArrayList<>();
            for (double[] point : polygon) {
                points.add(com.mapbox.geojson.Point.fromLngLat(point[0], point[1]));
            }
            points.add(points.get(0)); //add closing point
            polygonPoints.add(points);
        }
        return polygonPoints;
    }
}
