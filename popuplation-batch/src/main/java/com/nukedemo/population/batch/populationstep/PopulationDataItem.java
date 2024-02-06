package com.nukedemo.population.batch.populationstep;

import com.nukedemo.TiffPopulationDataContainer;
import lombok.Data;

@Data
public class PopulationDataItem {

    private final String dataName;

    private final byte[] tifSource;

    private TiffPopulationDataContainer populationImage;

    private String populationData;

}
