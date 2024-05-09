package com.nukedemo.population.batch.layersplittingstep;

import com.nukedemo.GhslMetaData;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LayerSplittingInputItem implements Serializable {

    private final String key;

    private final GhslMetaData metaData;

}