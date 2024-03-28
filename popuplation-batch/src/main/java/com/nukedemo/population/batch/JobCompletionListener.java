package com.nukedemo.population.batch;

import com.nukedemo.GhslMetaData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.nukedemo.shared.utils.NdJsonUtils.toJson;

@Slf4j
@Component
public class JobCompletionListener extends JobExecutionListenerSupport {

    private final String resultFolder;

    private final AtomicLong pixelWithValueCount;

    private final AtomicLong pixelTotalValue;

    private final ConcurrentHashMap<String, GhslMetaData> metaData;

    public JobCompletionListener(@Value("${populationBatch.ghsl.resultFolder}") String resultFolder) {
        this.resultFolder = resultFolder;
        this.pixelWithValueCount = new AtomicLong();
        this.pixelTotalValue = new AtomicLong();
        this.metaData = new ConcurrentHashMap<>();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("BATCH JOB COMPLETED SUCCESSFULLY");
            log.info("Total pixels with data count: " + pixelWithValueCount.get());
            log.info("Total data value: " + pixelTotalValue.get());
            writeMetaData();
        }
    }

    public void addCount (int count) {
        pixelWithValueCount.addAndGet(count);
    }

    public void addValue (int value) {
        pixelTotalValue.addAndGet(value);
    }

    public void addMetaData(String label, GhslMetaData ghslMetaData) {
        metaData.put(label, ghslMetaData);
    }

    private void writeMetaData() {
        try {
            FileUtils.writeStringToFile(new File(resultFolder, "metaData.json"), toJson(metaData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
