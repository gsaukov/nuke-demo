package com.nukedemo.dbscan;

import com.esri.core.geometry.Point;
import com.nukedemo.geocalculator.dbscan.DBSCAN;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class DBSCANTest {

    @Test
    public void DBSCANTest () {
        List<Point> points = new LinkedList<>();
        points.add(new Point(11.560385, 48.176874));
        points.add(new Point(11.560551, 48.177206));
        points.add(new Point(11.559370, 48.176901));
        points.add(new Point(11.557062, 48.176892));
        points.add(new Point(11.558105, 48.177123));
        points.add(new Point(11.557182, 48.177492));
        Set<List<Point>> results = DBSCAN.cluster(points, 75, 1);
    }

}