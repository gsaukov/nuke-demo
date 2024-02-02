package com.nukedemo.population.batch.ghslstep;

import com.nukedemo.TiffPopulationDataContainer;
import lombok.Data;

import java.io.File;

@Data
public class GhslFileDataItem {

    private final GhslFileInputItem inputItem;

    private final byte[] ghslData;

}
