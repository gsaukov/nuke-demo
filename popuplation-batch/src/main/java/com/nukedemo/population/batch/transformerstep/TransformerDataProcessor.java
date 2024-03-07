package com.nukedemo.population.batch.transformerstep;

import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.nukedemo.population.batch.JobCompletionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mapbox.geojson.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@StepScope
public class TransformerDataProcessor implements ItemProcessor<TransformerDataItem, TransformerDataItem> {

    @Autowired
    JobCompletionListener jobCompletionListener;

    public TransformerDataProcessor() {
    }

    @Override
    public TransformerDataItem process(TransformerDataItem item) {
        return item;
    }

    private List<Geometry> createPolygons (int[] data) {
        double top = 0; // lon
        double left = 0; // lat
        double height = 0;
        double width = 0;
        int rows = 100;
        int cols = 100;
        List<Geometry> squares = new ArrayList<>();

        for (int i = 0; i < 1200; i++) {
            for (int j = 0; j < 1200; j++) {
                double pixel = data[(i * cols) + j];
                if (pixel > 0) {
                    double verticalPos = top + (j * height);
                    double hotizontalPos = left - (i * width);
                    Point leftTop = Point.fromLngLat(verticalPos, hotizontalPos);

                    // Create a square feature
                    List<Point> points = new ArrayList<>();
                    points.add(leftTop);
                    points.add(Point.fromLngLat(verticalPos, hotizontalPos - height));
                    points.add(Point.fromLngLat(verticalPos + width, hotizontalPos - height));
                    points.add(Point.fromLngLat(verticalPos + width, hotizontalPos));
                    points.add(leftTop);
                    squares.add(Polygon.fromLngLats(Arrays.asList(points)));
                }
            }
        }
        return squares;
    }


}
