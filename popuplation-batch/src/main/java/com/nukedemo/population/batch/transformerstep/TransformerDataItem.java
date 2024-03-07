package com.nukedemo.population.batch.transformerstep;

import com.nukedemo.GhslMetaData;
import lombok.Data;

@Data
public class TransformerDataItem {

    private final GhslMetaData metaData;

    private final int[] intData;

}
