package com.nukedemo.population.batch.ghslstep;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class GhslFileStepConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    GhslFileDataReader ghslFileDataReader;

    @Autowired
    GhslFileDataWriter ghslFileDataWriter;

    @Autowired
    GhslFileDataPartitioner ghslFileDataPartitioner;

    public Step downloadingPartition() {
        return new StepBuilder("data-downloading-partition", jobRepository)
                .partitioner("downloading-partition-step", ghslFileDataPartitioner)
                .step(downloadingStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }


    private Step downloadingStep() {
        return new StepBuilder("data-downloading-step", jobRepository)
                .<GhslFileDataItem, GhslFileDataItem> chunk(1, transactionManager)
                .reader(ghslFileDataReader)
                .writer(ghslFileDataWriter)
                .build();
    }

    private TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
