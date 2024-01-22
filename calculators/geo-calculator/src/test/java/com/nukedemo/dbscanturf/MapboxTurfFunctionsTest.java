package com.nukedemo.dbscanturf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import com.nukedemo.geocalculator.dbscan.DBSCAN;
import com.nukedemo.geocalculator.dbscanturf.TPoint;
import org.apache.commons.math3.stat.clustering.DBSCANClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapboxTurfFunctionsTest {

    @Test
    public void testMapBoxTurf() {
        FeatureCollection f =  FeatureCollection.fromJson(param);
        double area = TurfMeasurement.area(f);
    }

    @Test
    public void testCenterCalculations() throws IOException, URISyntaxException, ParseException {
        String fileContent = new String(Files.readAllBytes(Paths.get("../../data/mil/usa.geojson")));
        FeatureCollection fc = FeatureCollection.fromJson(fileContent);
        System.out.println(TurfMeasurement.area(fc));
        Point sample = (Point)(TurfMeasurement.center(fc.features().get(0)).geometry());

        for (Feature f : fc.features()) {
            Geometry g = f.geometry();
            Point center = (Point)(TurfMeasurement.center(f).geometry());
            System.out.print("Type: " + g.type());
            System.out.print(" || center ");
            System.out.print(center.toJson());
            System.out.print(" || area ");
            System.out.print(TurfMeasurement.area(f));
            System.out.print(" || distance ");
            System.out.print(TurfMeasurement.distance(center, sample));
            System.out.println(" ");
        }
    }

    @Test
    public void testDBScanMapBoxTurf() throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get("../../data/mil/usa.geojson")));
        FeatureCollection fc = FeatureCollection.fromJson(fileContent);
        List<TPoint> points = new ArrayList<>();
        for (int i = 0; i < fc.features().size(); i++) {
            Feature feature = fc.features().get(i);
            Point center = (Point)(TurfMeasurement.center(feature).geometry());
            points.add(new TPoint(feature.id(), center, feature));
        }

        DBSCANClusterer dbscan = new DBSCANClusterer(1, 0);
        Long start = System.nanoTime();
        List<Cluster> clusters = dbscan.cluster(points);
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
    }


    @Test
    public void testDBSCANperf() throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get("../../data/mil/usa.geojson")));
        FeatureCollection fc = FeatureCollection.fromJson(fileContent);
        List<com.esri.core.geometry.Point> points = new ArrayList<>();
        for (int i = 0; i < fc.features().size(); i++) {
            Point center = (Point)(TurfMeasurement.center(fc.features().get(i)).geometry());
            points.add(new com.esri.core.geometry.Point(center.longitude(), center.latitude()));
        }
        Long start = System.nanoTime();
        Set<List<com.esri.core.geometry.Point>> results = DBSCAN.cluster(points, 1000, 0);
        Long end = System.nanoTime();
        List<List<com.esri.core.geometry.Point>> clusters = new ArrayList<>();
        clusters.addAll(results);
        System.out.println("For " + points.size() + " points calculated: " + clusters.size() + " clusters with exec time ms: " + ((end - start)/1000000));
        System.out.print("Individual clusters: ");
        int totalSize = 0;
        for (int i = 0; i < clusters.size(); i++) {
            int cSize = clusters.get(i).size();
            totalSize = totalSize + cSize;
            System.out.print(i +"[" + cSize + "], ");
            if(i != 0 && i % 30 == 0){
                System.out.println("");
            }
        }
    }

    private String param = "{\"type\":\"FeatureCollection\",\"generator\":\"overpass-ide\",\"copyright\":\"Thedataincludedinthisdocumentisfromwww.openstreetmap.org.ThedataismadeavailableunderODbL.\",\"timestamp\":\"2023-02-24T17:46:38Z\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"@id\":\"way/612169247\",\"building\":\"yes\",\"military\":\"barracks\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-104.775668,38.7362686],[-104.7756934,38.7362871],[-104.7758194,38.7361817],[-104.7758,38.7361676],[-104.77588,38.7361007],[-104.775854,38.7360818],[-104.7758628,38.7360745],[-104.775834,38.7360536],[-104.7758534,38.7360374],[-104.7758141,38.7360088],[-104.7757905,38.7360286],[-104.7757603,38.7360066],[-104.7756793,38.7360745],[-104.7757058,38.7360938],[-104.7756996,38.736099],[-104.7757226,38.7361157],[-104.7756787,38.7361525],[-104.7756499,38.7361315],[-104.7755699,38.7361984],[-104.7755979,38.7362188],[-104.7755698,38.7362424],[-104.7756391,38.7362927],[-104.775668,38.7362686]]]},\"id\":\"way/612169247\"}]}";
}