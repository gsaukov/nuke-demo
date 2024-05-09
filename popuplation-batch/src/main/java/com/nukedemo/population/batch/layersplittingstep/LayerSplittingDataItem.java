package com.nukedemo.population.batch.layersplittingstep;

import com.nukedemo.GhslMetaData;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.List;

@Data
public class LayerSplittingDataItem {

    private final List<List<BufferedImage>> block;

    private final LayerSplittingInputItem inputItem;

    private GhslMetaData metaData;

    private byte[] compressedLayer;

}
