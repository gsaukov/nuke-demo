package com.nukedemo.population.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class JobCompletionListener extends JobExecutionListenerSupport {

    private final AtomicLong pixelWithValueCount;

    private final AtomicLong pixelTotalValue;

    public JobCompletionListener() {
        pixelWithValueCount = new AtomicLong();
        pixelTotalValue = new AtomicLong();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("BATCH JOB COMPLETED SUCCESSFULLY");
            log.info("Total pixels with data count: " + pixelWithValueCount.get());
            log.info("Total data value: " + pixelTotalValue.get());
        }
    }

    public void addCount (int count) {
        pixelWithValueCount.addAndGet(count);
    }

    public void addValue (int value) {
        pixelTotalValue.addAndGet(value);
    }

}
