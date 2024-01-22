package com.nukedemo.geocalculator.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.geojson.*;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.turf.TurfMeasurement;
import com.nukedemo.geocalculator.dbscanturf.TPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.DBSCANClusterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GeoCalculatorApp {

    private static final String PROPERTY_PREFIX = "ND:";

    @Autowired
    private JtsCalculationService jtsCalculationService;

    public String calculate(String geojson) throws IOException {
        FeatureCollection featureCollection = FeatureCollection.fromJson(geojson);
        for (Feature feature : featureCollection.features()) {
            preCalculate(feature);
        }

//        featureCollection.
//        addProperty(feature, "center", center.toJson());
        List<Cluster<TPoint>> clusters = getDBScanCluster(featureCollection);
        clusterGeometry(clusters);
        return printPrettyJson(featureCollection);
    }

    private void preCalculate(Feature feature) {
        Geometry g = feature.geometry();
        Point center = (Point) (TurfMeasurement.center(feature).geometry());
        addProperty(feature, "center", center.toJson());
        addProperty(feature, "area", String.valueOf(TurfMeasurement.area(feature)));
    }

    private void clusterGeometry(List<Cluster<TPoint>> clusters) {
        for(Cluster<TPoint> cluster : clusters){
            List<Feature> features = new ArrayList<>();
            for(TPoint point : cluster.getPoints()) {
                features.add(point.getFeature());
            }
            try {
                Geometry g = jtsCalculationService.union(features);
                if(g != null) {
                    System.out.println(g.toJson());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void addProperty(Feature feature, String name, String value) {
        feature.addStringProperty(PROPERTY_PREFIX + name, value);

    }

    private String printPrettyJson(FeatureCollection json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GeoJsonAdapterFactory.create())
                .registerTypeAdapterFactory(GeometryAdapterFactory.create())
                .setPrettyPrinting()
                .create();
        return gson.toJson(json);
    }

    public List<Cluster<TPoint>> getDBScanCluster(FeatureCollection fc) {
        List<TPoint> points = new ArrayList<>();
        for (int i = 0; i < fc.features().size(); i++) {
            Feature feature = fc.features().get(i);
            Point center = (Point)(TurfMeasurement.center(feature).geometry());
            points.add(new TPoint(feature.id(), center, feature));
        }

        DBSCANClusterer dbscan = new DBSCANClusterer(2, 0);
        Long start = System.nanoTime();
        List<Cluster<TPoint>> clusters = dbscan.cluster(points);
        Long end = System.nanoTime();
        System.out.println("For " + points.size() + " points calculated: " + clusters.size() + " clusters with exec time ms: " + ((end - start)/1000000));
        int totalSize = 0;
        System.out.print("Individual clusters: ");
        for (int i = 0; i < clusters.size(); i++) {
            int cSize = clusters.get(i).getPoints().size();
            totalSize = totalSize + cSize;
            System.out.print(i +"[" + cSize + "], ");
            if(i != 0 && i % 30 == 0){
                System.out.println("");
            }
        }
        System.out.println(System.lineSeparator() + "Total nodes points included into cluster: " + totalSize);
        return clusters;
    }


}
