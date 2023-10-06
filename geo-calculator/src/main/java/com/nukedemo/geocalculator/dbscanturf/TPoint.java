package com.nukedemo.geocalculator.dbscanturf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import org.apache.commons.math3.stat.clustering.Clusterable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TPoint implements Serializable, Clusterable<TPoint> {

    private final String id;

    private final Point point;

    private final Feature feature;

    public TPoint(String id, Point point, Feature feature) {
        this.id = id;
        this.point = point;
        this.feature = feature;
    }

    public String getId() {
        return id;
    }

    public Point getPoint() {
        return point;
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    public double distanceFrom(TPoint p) {
        return TurfMeasurement.distance(point, p.point);
    }

    @Override
    public TPoint centroidOf(Collection<TPoint> collection) {
        List<Feature> points = collection.stream().map(e -> Feature.fromGeometry(e.point)).collect(Collectors.toList());
        Feature center = TurfMeasurement.center(FeatureCollection.fromFeatures(points));
        return new TPoint("center", (Point) center.geometry(), null);
    }
}
