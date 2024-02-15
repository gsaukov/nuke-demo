package com.nukedemo.population.batch.populationstep;

import com.nukedemo.GhslMetaData;
import com.nukedemo.TiffPopulationDataContainer;
import com.nukedemo.population.batch.JobCompletionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@StepScope
public class PopulationDataProcessor implements ItemProcessor<PopulationDataItem, PopulationDataItem> {

    @Autowired
    JobCompletionListener jobCompletionListener;

    public PopulationDataProcessor() {
    }

    @Override
    public PopulationDataItem process(PopulationDataItem item) throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(item.getTifSource());
        GhslMetaData metaData = container.getMetaData();
        updateGlobalMetadata(metaData);
        item.setMetaData(metaData);
        item.setPopulationDataInt(new PopulationDataItem.PopulationDataInt(container.toIntArray()));
        item.setPopulationDataDouble(new PopulationDataItem.PopulationDataDouble(container.toDoubleArray()));
        return item;
    }

    private void updateGlobalMetadata(GhslMetaData metaData) {
        jobCompletionListener.addCount(metaData.getTotalPixelCount());
        jobCompletionListener.addValue(metaData.getTotalPixelValue());
    }


}
