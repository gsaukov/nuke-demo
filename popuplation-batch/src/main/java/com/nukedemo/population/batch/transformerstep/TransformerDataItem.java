package com.nukedemo.population.batch.transformerstep;

import com.mapbox.geojson.Geometry;
import com.nukedemo.GhslMetaData;
import lombok.Data;

@Data
public class TransformerDataItem {

    private final GhslMetaData metaData;

    private final int[] intData;

    private Geometry transformedGeometry;

}
