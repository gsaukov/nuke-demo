package com.nukedemo.population.batch.populationstep;

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
    public PopulationDataItem process(PopulationDataItem item) {
        item.setPopulationData("DUMMY");
        return item;
    }
}
