package com.nukedemo.population.batch.layercompressionstep;


import com.nukedemo.GhslMetaData;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionStepConfiguration.TARGET_RESOLUTION;
import static com.nukedemo.population.batch.populationstep.PopulationDataWriter.POPULATION_IMG_FOLDER;
import static com.nukedemo.shared.utils.NdJsonUtils.toJson;

@Component
@StepScope
public class LayerCompressionStepCompletionListener implements StepExecutionListener {

    @Value("${populationBatch.ghsl.basePath}")
    private int basePath;

    private final ConcurrentHashMap<String, GhslMetaData> metaData;

    public LayerCompressionStepCompletionListener() {
        this.metaData = new ConcurrentHashMap<>();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            writeMetaData();
        }
        return ExitStatus.COMPLETED;
    }

    public void addMetaData(String label, GhslMetaData ghslMetaData) {
        metaData.put(label, ghslMetaData);
    }

    private void writeMetaData() {
        try {
            File outputFolder = new File(basePath + "/" + TARGET_RESOLUTION + "/" + POPULATION_IMG_FOLDER);
            FileUtils.writeStringToFile(new File(outputFolder, "metaData.json"), toJson(metaData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
