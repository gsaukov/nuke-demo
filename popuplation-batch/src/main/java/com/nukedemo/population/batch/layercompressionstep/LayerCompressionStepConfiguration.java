package com.nukedemo.population.batch.layercompressionstep;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class LayerCompressionStepConfiguration implements Cloneable {

    public static final String SOURCE_PATH = "./data/res/population/ghsl";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    LayerCompressionDataReader layerCompressionDataReader;

    @Autowired
    LayerCompressionDataProcessor layerCompressionDataProcessor;

    @Autowired
    LayerCompressionDataWriter layerCompressionDataWriter;

    @Autowired
    LayerCompressionDataPartitioner layerCompressionDataPartitioner;

    public Step layerCompressionPartition() {

        return new StepBuilder("layer-compression-partition", jobRepository)
                .partitioner("layer-compression-partition-step", layerCompressionDataPartitioner)
                .step(layerCompressionStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }


    private Step layerCompressionStep() {
        return new StepBuilder("layer-compression-step", jobRepository)
                .<LayerCompressionDataItem, LayerCompressionDataItem> chunk(1, transactionManager)
                .reader(layerCompressionDataReader)
                .processor(layerCompressionDataProcessor)
                .writer(layerCompressionDataWriter)
                .build();
    }
    private TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

}
