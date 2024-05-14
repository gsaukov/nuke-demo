package com.nukedemo.population.batch.layersplittingstep;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.Map;

@Data
public class LayerSplittingDataItem {

    private final LayerSplittingInputItem inputItem;

    private final BufferedImage source;

    private Map<String, byte[]> splitImages;

}
