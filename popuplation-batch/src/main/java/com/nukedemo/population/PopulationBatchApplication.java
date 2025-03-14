package com.nukedemo.population;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

@EnableFeignClients
@ComponentScan("com.nukedemo.*")
@EnableAutoConfiguration
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class PopulationBatchApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PopulationBatchApplication.class, args);
        JobLauncher joblauncher = context.getBean("jobLauncher", JobLauncher.class);
        Job job = context.getBean("population-processing-job", Job.class);
        try {
            joblauncher.run(job, new JobParametersBuilder().addDate("dt", new Date()).toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        System.exit(SpringApplication.exit(context));
    }

}
