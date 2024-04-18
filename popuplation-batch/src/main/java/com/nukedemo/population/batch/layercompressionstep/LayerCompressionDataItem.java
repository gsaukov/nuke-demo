package com.nukedemo.population.batch.layercompressionstep;

import com.nukedemo.GhslMetaData;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.List;

@Data
public class LayerCompressionDataItem {

    private final List<List<BufferedImage>> block;

    private final LayerCompressionInputItem inputItem;

    private GhslMetaData metaData;

    private byte[] compressedLayer;

}
