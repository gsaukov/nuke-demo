package com.nukedemo.population.batch.layersplittingstep;

import com.nukedemo.GhslMetaData;
import com.nukedemo.ImageTransformations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@StepScope
public class LayerSplittingDataProcessor implements ItemProcessor<LayerSplittingDataItem, LayerSplittingDataItem> {

    @Autowired
    LayerSplittingStepCompletionListener layerSplittingStepCompletionListener;

    public LayerSplittingDataProcessor() {
    }

    @Override
    public LayerSplittingDataItem process(LayerSplittingDataItem item) throws IOException {
        return item;
    }


}
