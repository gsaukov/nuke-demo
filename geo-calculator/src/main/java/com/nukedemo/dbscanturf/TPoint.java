package com.nukedemo.dbscanturf;

import com.mapbox.geojson.Point;
import org.apache.commons.math3.stat.clustering.Clusterable;

import java.io.Serializable;
import java.util.Collection;

public class TPoint implements Serializable, Clusterable<TPoint> {

    private final Point point;
    private final int id;

    public TPoint(int id, Point point) {
        this.point = point;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public double distanceFrom(TPoint p) {
        return 0;
    }

    @Override
    public TPoint centroidOf(Collection<TPoint> collection) {
        return null;
    }
}
