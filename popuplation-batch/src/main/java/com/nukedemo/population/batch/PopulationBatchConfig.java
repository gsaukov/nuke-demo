package com.nukedemo.population.batch;

import com.nukedemo.population.batch.ghslstep.GhslFileStepConfiguration;
import com.nukedemo.population.batch.layercompressionstep.LayerCompressionStepConfiguration;
import com.nukedemo.population.batch.populationstep.PopulationStepConfiguration;
import com.nukedemo.population.batch.transformerstep.TransformerStepConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class PopulationBatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    JobCompletionListener jobCompletionListener;

    @Autowired
    GhslFileStepConfiguration ghslFileStep;

    @Autowired
    PopulationStepConfiguration populationStep;

    @Autowired
    TransformerStepConfiguration transformerStep;

    @Autowired
    LayerCompressionStepConfiguration layerCompressionStep;

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean("population-processing-job")
    public Job processJob() {
        return new JobBuilder("population-processing-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .flow(ghslFileStep.downloadingPartition())
                .next(populationStep.processingPartition())
                .next(transformerStep.transformerPartition())
                .next(layerCompressionStep.layerCompressionPartition())
                .end()
                .build();
    }

}
