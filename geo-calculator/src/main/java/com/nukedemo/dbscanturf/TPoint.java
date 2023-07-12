package com.nukedemo.dbscanturf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.hamcrest.Factory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TPoint implements Serializable, Clusterable<TPoint> {

    private final Point point;
    private final String id;

    public TPoint(String id, Point point) {
        this.point = point;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public double distanceFrom(TPoint p) {
        return TurfMeasurement.distance(point, p.point);
    }

    @Override
    public TPoint centroidOf(Collection<TPoint> collection) {
        List<Feature> points = collection.stream().map(e -> Feature.fromGeometry(e.point)).collect(Collectors.toList());
        Feature center = TurfMeasurement.center(FeatureCollection.fromFeatures(points));
        return new TPoint("center", (Point) center.geometry());
    }
}
