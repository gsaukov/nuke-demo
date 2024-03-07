package com.nukedemo.population.batch.transformerstep;

import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.nukedemo.GhslMetaData;
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
        createPolygons(item);
        return item;
    }

    private List<Geometry> createPolygons(TransformerDataItem item) {
        int[] data = item.getIntData();
        GhslMetaData metaData = item.getMetaData();
        double top = metaData.getTopLeftCorner()[0]; // lon
        double left = metaData.getTopLeftCorner()[1]; // lat
        double height = metaData.getPixelHeightDegrees();
        double width = metaData.getPixelWidthDegrees();
        int rows = metaData.getAreaHeight();
        int cols = metaData.getAreaWidth();
        List<Geometry> squares = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double pixel = data[(i * cols) + j];
                if (pixel > 0) {
                    double verticalPos = top + (j * height);
                    double hotizontalPos = left - (i * width);
                    Point leftTop = Point.fromLngLat(verticalPos, hotizontalPos);
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
