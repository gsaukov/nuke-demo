package com.nukedemo.population.batch.layercompressionstep;

import com.nukedemo.population.batch.JobCompletionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@StepScope
public class LayerCompressionDataProcessor implements ItemProcessor<LayerCompressionDataItem, LayerCompressionDataItem> {

    @Autowired
    JobCompletionListener jobCompletionListener;

    public LayerCompressionDataProcessor() {
    }

    @Override
    public LayerCompressionDataItem process(LayerCompressionDataItem item) {
        return item;
    }

}
