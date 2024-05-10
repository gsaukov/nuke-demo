package com.nukedemo.population.batch.layersplittingstep;

import com.nukedemo.GhslMetaData;
import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
public class LayerSplittingInputItem implements Serializable {

    private final String key;

    private final GhslMetaData metaData;

    private final File file;

}