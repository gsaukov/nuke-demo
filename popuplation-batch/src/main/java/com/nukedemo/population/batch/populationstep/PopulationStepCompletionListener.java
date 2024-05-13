package com.nukedemo.population.batch.populationstep;

import com.nukedemo.GhslMetaData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.nukedemo.shared.utils.NdJsonUtils.toJson;

@Slf4j
@Component
public class PopulationStepCompletionListener implements StepExecutionListener {

    private final String resultFolder;

    private boolean skipProcessing;

    private final AtomicLong pixelWithValueCount;

    private final AtomicLong pixelTotalValue;

    private final ConcurrentHashMap<String, GhslMetaData> metaData;

    public PopulationStepCompletionListener(@Value("${populationBatch.ghsl.resultFolder}") String resultFolder, @Value("${populationBatch.ghsl.skipProcessing}") boolean skipProcessing) {
        this.skipProcessing = skipProcessing;
        this.resultFolder = resultFolder;
        this.pixelWithValueCount = new AtomicLong();
        this.pixelTotalValue = new AtomicLong();
        this.metaData = new ConcurrentHashMap<>();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!skipProcessing && stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("POPULATION STEP COMPLETED SUCCESSFULLY");
            log.info("Total pixels with data count: " + pixelWithValueCount.get());
            log.info("Total data value: " + pixelTotalValue.get());
            writeMetaData();
        }
        return ExitStatus.COMPLETED;
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
