package com.nukedemo.population.batch.ghslstep;

import com.nukedemo.TiffPopulationDataContainer;
import lombok.Data;

import java.io.File;

@Data
public class GhslFileDataItem {

    File ghslFile;

    public GhslFileDataItem(File file) {
        this.ghslFile = file;
    }

}
