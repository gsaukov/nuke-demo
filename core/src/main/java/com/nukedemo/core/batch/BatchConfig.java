package com.nukedemo.core.batch;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;


@Configuration
//@EnableAutoConfiguration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job processJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("geo-data-processing-job", jobRepository).incrementer(new RunIdIncrementer()).listener(listener())
                .flow(step).end().build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("data-gathering-step", jobRepository).<Object, Object> chunk(1, transactionManager)
                .reader(new GeoDataReader())
                .processor(new GeoDataProcessor())
                .writer(new GeoDataWriter())
                .build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }

}
