package com.nukedemo.population.batch.transformerstep;

import com.nukedemo.population.batch.populationstep.*;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class TransformerStepConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    TransformerDataReader transformerDataReader;

    @Autowired
    TransformerDataProcessor transformerDataProcessor;

    @Autowired
    TransformerDataWriter transformerDataWriter;

    @Autowired
    TransformerDataPartitioner transformerDataPartitioner;


    public Step transformerPartition() {
        return new StepBuilder("data-transforming-partition", jobRepository)
                .partitioner("transforming-partition-step", transformerDataPartitioner)
                .step(transformerStep())
                .gridSize(1)
                .taskExecutor(taskExecutor())
                .build();
    }


    private Step transformerStep() {
        return new StepBuilder("data-transforming-step", jobRepository)
                .<TransformerDataItem, TransformerDataItem> chunk(1, transactionManager)
                .reader(transformerDataReader)
                .processor(transformerDataProcessor)
                .writer(transformerDataWriter)
                .build();
    }
    private TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

}
