package com.nukedemo.core.batch;
import com.nukedemo.core.batch.inputmodel.InputItem;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private static final List<InputItem> INJECTED_IN_CONTEXT = null;

    @Bean
    public JobLauncher  jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean("geo-data-processing-job")
    public Job processJob() {
        return new JobBuilder("geo-data-processing-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(partitionStep())
                .end()
                .build();
    }

    @Bean
    public Step partitionStep() {
        return new StepBuilder("data-processing-partitioning", jobRepository)
                .partitioner("partition-step", new GeoDataPartitioner())
                .step(step())
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step() {
        return new StepBuilder("data-processing-step", jobRepository)
                .<String, GeoDataItem> chunk(1, transactionManager)
                .reader(geoDataReader(INJECTED_IN_CONTEXT))
                .processor(geoDataProcessor())
                .writer(geoDataWriter())
                .build();
    }

    @Bean
    @StepScope
    public GeoDataReader geoDataReader(
            @Value("#{stepExecutionContext['country']}") List<InputItem> countries) {
        return new GeoDataReader(countries);
    }

    @Bean
    @StepScope
    public GeoDataProcessor geoDataProcessor() {
        return new GeoDataProcessor();
    }

    @Bean
    @StepScope
    public GeoDataWriter geoDataWriter() {
        return new GeoDataWriter();
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
