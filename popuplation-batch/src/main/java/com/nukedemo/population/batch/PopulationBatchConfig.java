package com.nukedemo.population.batch;
import com.nukedemo.population.batch.ghslstep.GhslFileDataItem;
import com.nukedemo.population.batch.ghslstep.GhslFileDataPartitioner;
import com.nukedemo.population.batch.ghslstep.GhslFileDataReader;
import com.nukedemo.population.batch.ghslstep.GhslFileDataWriter;
import com.nukedemo.population.batch.populationstep.*;
import com.nukedemo.population.batch.transformerstep.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class PopulationBatchConfig {

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

    @Autowired
    PopulationDataReader populationDataReader;

    @Autowired
    PopulationDataProcessor populationDataProcessor;

    @Autowired
    PopulationDataWriter populationDataWriter;

    @Autowired
    PopulationDataPartitioner populationDataPartitioner;

    @Autowired
    TransformerDataReader transformerDataReader;

    @Autowired
    TransformerDataProcessor transformerDataProcessor;

    @Autowired
    TransformerDataWriter transformerDataWriter;

    @Autowired
    TransformerDataPartitioner transformerDataPartitioner;

    @Bean
    public JobLauncher  jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean("population-processing-job")
    public Job processJob() {
        return new JobBuilder("population-processing-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(downloadingPartition())
                .next(processingPartition())
                .next(transformerPartition())
                .end()
                .build();
    }

    @Bean
    public Step downloadingPartition() {
        return new StepBuilder("data-downloading-partition", jobRepository)
                .partitioner("downloading-partition-step", ghslFileDataPartitioner)
                .step(downloadingStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step downloadingStep() {
        return new StepBuilder("data-downloading-step", jobRepository)
                .<GhslFileDataItem, GhslFileDataItem> chunk(1, transactionManager)
                .reader(ghslFileDataReader)
                .writer(ghslFileDataWriter)
                .build();
    }

    @Bean
    public Step processingPartition() {
        return new StepBuilder("data-processing-partition", jobRepository)
                .partitioner("processing-partition-step", populationDataPartitioner)
                .step(processingStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step processingStep() {
        return new StepBuilder("data-processing-step", jobRepository)
                .<PopulationDataItem, PopulationDataItem> chunk(1, transactionManager)
                .reader(populationDataReader)
                .processor(populationDataProcessor)
                .writer(populationDataWriter)
                .build();
    }

    @Bean
    public Step transformerPartition() {
        return new StepBuilder("data-transforming-partition", jobRepository)
                .partitioner("transforming-partition-step", transformerDataPartitioner)
                .step(transformerStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step transformerStep() {
        return new StepBuilder("data-transforming-step", jobRepository)
                .<TransformerDataItem, TransformerDataItem> chunk(1, transactionManager)
                .reader(transformerDataReader)
                .processor(transformerDataProcessor)
                .writer(transformerDataWriter)
                .build();
    }

    @Bean
    public JobCompletionListener listener() {
        return new JobCompletionListener();
    }

    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
