package com.nukedemo.population.batch.transformerstep;

import com.nukedemo.population.batch.JobCompletionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@StepScope
public class TransformerDataProcessor implements ItemProcessor<TransformerDataItem, TransformerDataItem> {

    @Autowired
    JobCompletionListener jobCompletionListener;

    public TransformerDataProcessor() {
    }

    @Override
    public TransformerDataItem process(TransformerDataItem item) {
        return item;
    }


}
