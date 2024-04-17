package com.nukedemo.population.batch.populationstep;

import com.nukedemo.population.batch.ghslstep.GhslFileDataItem;
import com.nukedemo.population.batch.ghslstep.GhslFileDataPartitioner;
import com.nukedemo.population.batch.ghslstep.GhslFileDataReader;
import com.nukedemo.population.batch.ghslstep.GhslFileDataWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class PopulationStepConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    PopulationDataReader populationDataReader;

    @Autowired
    PopulationDataProcessor populationDataProcessor;

    @Autowired
    PopulationDataWriter populationDataWriter;

    @Autowired
    PopulationDataPartitioner populationDataPartitioner;


    public Step processingPartition() {
        return new StepBuilder("data-processing-partition", jobRepository)
                .partitioner("processing-partition-step", populationDataPartitioner)
                .step(processingStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    private Step processingStep() {
        return new StepBuilder("data-processing-step", jobRepository)
                .<PopulationDataItem, PopulationDataItem> chunk(1, transactionManager)
                .reader(populationDataReader)
                .processor(populationDataProcessor)
                .writer(populationDataWriter)
                .build();
    }

    private TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

}
