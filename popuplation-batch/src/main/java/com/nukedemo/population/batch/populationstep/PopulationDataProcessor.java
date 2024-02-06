package com.nukedemo.population.batch.populationstep;

import com.nukedemo.TiffPopulationDataContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@StepScope
public class PopulationDataProcessor implements ItemProcessor<PopulationDataItem, PopulationDataItem> {

    public PopulationDataProcessor() {
    }

    @Override
    public PopulationDataItem process(PopulationDataItem item) throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(item.getTifSource());
        item.setPopulationData("{\"res\":[\n" + container.toStringIntArray() + "\n}");
        return item;
    }
}
