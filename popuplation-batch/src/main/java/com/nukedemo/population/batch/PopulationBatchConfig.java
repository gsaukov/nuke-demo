package com.nukedemo.population.batch;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
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
    PopulationDataReader populationDataReader;

    @Autowired
    PopulationDataProcessor populationDataProcessor;

    @Autowired
    PopulationDataWriter populationDataWriter;

    @Autowired
    PopulationDataPartitioner populationDataPartitioner;

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
                .end()
                .build();
    }

    @Bean
    public Step downloadingPartition() {
        return new StepBuilder("data-downloading-partition", jobRepository)
                .partitioner("downloading-partition-step", populationDataPartitioner)
                .step(processingStep())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step downloadingStep() {
        return new StepBuilder("data-downloading-step", jobRepository)
                .<PopulationDataItem, PopulationDataItem> chunk(1, transactionManager)
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
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }

}
