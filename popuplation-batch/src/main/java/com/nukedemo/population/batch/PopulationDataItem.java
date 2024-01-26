package com.nukedemo.population.batch;

import lombok.Data;

@Data
public class PopulationDataItem {

    private String areaCode;

    private String populationImage;

    private String populationData;

    public PopulationDataItem(String areaCode) {
        this.areaCode = areaCode;
    }

}
