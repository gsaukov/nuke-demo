package com.nukedemo.population.batch.populationstep;

import com.nukedemo.TiffPopulationDataContainer;
import lombok.Data;

@Data
public class PopulationDataItem {

    private final String dataName;

    private final byte[] tifSource;

    private TiffPopulationDataContainer populationImage;

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

    @Data
    public static class PopulationMetaData {
        private final int width;
        private final int height;
        private final double topY;
        private final double topX;
        private final double bottomY;
        private final double bottomX;
        private final double grain;
    }

}


