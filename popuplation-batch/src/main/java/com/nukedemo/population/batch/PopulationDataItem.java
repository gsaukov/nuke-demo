package com.nukedemo.population.batch;

import com.nukedemo.TiffPopulationDataContainer;
import lombok.Data;

@Data
public class PopulationDataItem {

    private String areaCode;

    private TiffPopulationDataContainer populationImage;

    private String populationData;

    public PopulationDataItem(String areaCode) {
        this.areaCode = areaCode;
    }

}
