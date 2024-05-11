package com.nukedemo.population.batch.layersplittingstep;

import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class LayerSplittingDataItem {

    private final LayerSplittingInputItem inputItem;

    private final BufferedImage source;

}
