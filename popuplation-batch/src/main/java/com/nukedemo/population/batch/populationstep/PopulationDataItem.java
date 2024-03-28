package com.nukedemo.population.batch.populationstep;

import com.nukedemo.GhslMetaData;
import com.nukedemo.TiffPopulationDataContainer;
import lombok.Data;

@Data
public class PopulationDataItem {

    private final String dataName;

    private final byte[] tifSource;

    private TiffPopulationDataContainer populationDataContainer;

    private byte[] pngSource;

    private GhslMetaData metaData;

    private PopulationDataInt populationDataInt;

    private PopulationDataDouble populationDataDouble;

    @Data
    public static class PopulationDataInt {
        private final int[] res;
    }

    @Data
    public static class PopulationDataDouble {
        private final double[] res;
    }

}


