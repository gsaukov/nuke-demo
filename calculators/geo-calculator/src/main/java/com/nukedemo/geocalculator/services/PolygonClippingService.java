package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.Feature;
import com.menecats.polybool.Epsilon;
import com.menecats.polybool.PolyBool;
import com.menecats.polybool.models.Polygon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.menecats.polybool.helpers.PolyBoolHelper.*;
import static com.menecats.polybool.helpers.PolyBoolHelper.point;

@Slf4j
@Service
public class PolygonClippingService {

    private static final Epsilon EPSILON = epsilon();

    public com.mapbox.geojson.Geometry union(List<Feature> features) throws Exception {
        List<List<List<List<double[]>>>> polygons = extractFeaturesCoordinates(features);
        if (polygons.isEmpty()) {
            return null;
        }
        List<List<List<double[]>>> cumulative = polygons.get(0);
        if (polygons.size() < 2) {
            return toTurfGeometry(cumulative);
        }
        for (int i = 1; i < polygons.size(); i++) {//skip first
//            cumulative = PolyBool.union(EPSILON, cumulative, polygons.get(i));;
        }
        return toTurfGeometry(cumulative);
    }

    public com.mapbox.geojson.Geometry toTurfGeometry(List<List<List<double[]>>> cumulative) {
        List<com.mapbox.geojson.Polygon> resPolygons = new ArrayList<>();
        List<List<com.mapbox.geojson.Point>> polygons = createPointsFromLngLatsWithHoles(cumulative);
        com.mapbox.geojson.Polygon.fromLngLats(polygons);
        return com.mapbox.geojson.MultiPolygon.fromPolygons(new ArrayList<>());
    }

    public List<List<List<List<double[]>>>> extractFeaturesCoordinates(List<com.mapbox.geojson.Feature> features) {
        List<List<List<List<double[]>>>> polygons = new ArrayList<>();
        for (com.mapbox.geojson.Feature feature : features) {
            polygons.add(extractFeatureCoordinates(feature));
        }
        return polygons;
    }

    public List<List<List<double[]>>> extractFeatureCoordinates(com.mapbox.geojson.Feature feature) {
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
        List<com.mapbox.geojson.Point> ring = polygon.coordinates().get(0);

        List<double[]> ringList = new ArrayList<>();
        for (com.mapbox.geojson.Point coordinate : ring) {
            ringList.add(new double[]{coordinate.longitude(), coordinate.latitude()});  // Swap order for lat/lng
        }
        polygonCoordinates.add(ringList);

        return polygonCoordinates;
    }

    public static List<List<com.mapbox.geojson.Point>> createPointsFromLngLatsWithHoles(List<List<List<double[]>>> lngLatLists) {
        List<List<com.mapbox.geojson.Point>> pointsWithHoles = new ArrayList<>();
        for (List<List<double[]>> polygonLngLats : lngLatLists) {
            for (List<double[]> ringLngLats : polygonLngLats) {
                pointsWithHoles.add(createPointsFromLngLats(List.of(ringLngLats))); // Convert to single-element list
            }
        }
        return pointsWithHoles;
    }

    public static List<com.mapbox.geojson.Point> createPointsFromLngLats(List<List<double[]>> lngLatLists) {
        List<com.mapbox.geojson.Point> points = new ArrayList<>();
        for (List<double[]> lngLatList : lngLatLists) {
            double[] lngLat = lngLatList.get(0);
            points.add(com.mapbox.geojson.Point.fromLngLat(lngLat[0], lngLat[1]));
        }
        return points;
    }

    private void sample() {
        List<Polygon> pols = Arrays.asList(
                polygon(
                        region(
                                point(9.992083316153526, 49.09958333862941),
                                point(9.992083316153526, 49.09124917199628),
                                point(10.000417482787183, 49.09124917199628),
                                point(10.000417482787183, 49.09958333862941),
                                point(9.992083316153526, 49.09958333862941)
                        )),
                polygon(
                        region(
                                point(10.00041664945332, 49.09958333862941),
                                point(10.00041664945332, 49.09124917199628),
                                point(10.008750816086978, 49.09124917199628),
                                point(10.008750816086978, 49.09958333862941),
                                point(10.00041664945332, 49.09958333862941)
                        )),
                polygon(
                        region(
                                point(10.008749982753116, 49.09958333862941),
                                point(10.008749982753116, 49.09124917199628),
                                point(10.017084149386774, 49.09124917199628),
                                point(10.017084149386774, 49.09958333862941),
                                point(10.008749982753116, 49.09958333862941)
                        )),
                polygon(
                        region(
                                point(9.992083316153526, 49.09125000532908),
                                point(9.992083316153526, 49.08291583869595),
                                point(10.000417482787183, 49.08291583869595),
                                point(10.000417482787183, 49.09125000532908),
                                point(9.992083316153526, 49.09125000532908)
                        )),
                polygon(
                        region(
                                point(10.008749982753116, 49.09125000532908),
                                point(10.008749982753116, 49.08291583869595),
                                point(10.017084149386774, 49.08291583869595),
                                point(10.017084149386774, 49.09125000532908),
                                point(10.008749982753116, 49.09125000532908)
                        )),
                polygon(
                        region(
                                point(9.992083316153526, 49.08291667202875),
                                point(9.992083316153526, 49.074582505395625),
                                point(10.000417482787183, 49.074582505395625),
                                point(10.000417482787183, 49.08291667202875),
                                point(9.992083316153526, 49.08291667202875)
                        )),
                polygon(
                        region(
                                point(10.00041664945332, 49.08291667202875),
                                point(10.00041664945332, 49.074582505395625),
                                point(10.008750816086978, 49.074582505395625),
                                point(10.008750816086978, 49.08291667202875),
                                point(10.00041664945332, 49.08291667202875)
                        )),
                polygon(
                        region(
                                point(10.008749982753116, 49.08291667202875),
                                point(10.008749982753116, 49.074582505395625),
                                point(10.017084149386774, 49.074582505395625),
                                point(10.017084149386774, 49.08291667202875),
                                point(10.008749982753116, 49.08291667202875)
                        ))

        );

        Polygon cum = pols.get(0);

        for (int i = 1; i < pols.size(); i++) {
            cum = PolyBool.union(EPSILON, cum, pols.get(i));
        }
        log.info(cum.toString());
    }
}
