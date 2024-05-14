package com.nukedemo.population.batch.layersplittingstep;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class LayerSplittingStepConfiguration implements Cloneable {


    public static final int SPLIT_FACTOR = 4;
    public static final String ORIGINAL_RESOLUTION = "4326_3ss";
    public static final String TARGET_RESOLUTION = "4326_3ss_c";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    LayerSplittingDataReader layerSplittingDataReader;

    @Autowired
    LayerSplittingDataProcessor layerSplittingDataProcessor;

    @Autowired
    LayerSplittingDataWriter layerSplittingDataWriter;

    @Autowired
    LayerSplittingDataPartitioner layerSplittingDataPartitioner;

    @Autowired
    LayerSplittingStepCompletionListener layerSplittingStepCompletionListener;

    public Step layerSplittingPartition() {
        return new StepBuilder("layer-splitting-partition", jobRepository)
                .partitioner("layer-splitting-partition-step", layerSplittingDataPartitioner)
                .step(layerSplittingStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .listener(layerSplittingStepCompletionListener)
                .build();
    }


    private Step layerSplittingStep() {
        return new StepBuilder("layer-splitting-step", jobRepository)
                .<LayerSplittingDataItem, LayerSplittingDataItem> chunk(1, transactionManager)
                .reader(layerSplittingDataReader)
                .processor(layerSplittingDataProcessor)
                .writer(layerSplittingDataWriter)
                .build();
    }
    private TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

}
