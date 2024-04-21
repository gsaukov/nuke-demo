package com.nukedemo.population.batch.layercompressionstep;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LayerCompressionInputItem implements Serializable {

    private final int row;

    private final int column;

    private final List<List<String>> block;

    //TopLeft, BottomRight [lon,lat]
    private final double [][] blockDimensions;

}