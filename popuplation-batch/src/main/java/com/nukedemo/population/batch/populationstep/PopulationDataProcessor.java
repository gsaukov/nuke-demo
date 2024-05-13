package com.nukedemo.population.batch.populationstep;

import com.nukedemo.GhslMetaData;
import com.nukedemo.TiffPngConverter;
import com.nukedemo.TiffPopulationDataContainer;
import com.nukedemo.population.batch.JobCompletionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@StepScope
public class PopulationDataProcessor implements ItemProcessor<PopulationDataItem, PopulationDataItem> {

    @Value("${populationBatch.ghsl.resolution}")
    private String resolution;

    @Autowired
    PopulationStepCompletionListener stepCompletionListener;

    public PopulationDataProcessor() {
    }

    @Override
    public PopulationDataItem process(PopulationDataItem item) throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(item.getTifSource());
        GhslMetaData metaData = container.getMetaData();
        updateGlobalMetadata(item.getDataName(), metaData);
        item.setMetaData(metaData);
        int density = resolveDensity();
        item.setPngSource(container.writeToPngByteArray(density));
        if (density == TiffPngConverter.DENSITY_30SS) { // this 12000 x 12000 resolution JSON is too big
            item.setPopulationDataInt(new PopulationDataItem.PopulationDataInt(container.toIntArray()));
            item.setPopulationDataDouble(new PopulationDataItem.PopulationDataDouble(container.toDoubleArray()));
        }
        return item;
    }

    private void updateGlobalMetadata(String dataName, GhslMetaData metaData) {
        stepCompletionListener.addCount(metaData.getTotalPixelCount());
        stepCompletionListener.addValue(metaData.getTotalPixelValue());
        stepCompletionListener.addMetaData(dataName, metaData);
    }

    private int resolveDensity() {
        switch(resolution) {
            case "4326_30ss":
                return TiffPngConverter.DENSITY_30SS;
            case "4326_3ss":
                return TiffPngConverter.DENSITY_3SS;
            default:
                throw new UnsupportedOperationException(resolution);
        }
    }


}
