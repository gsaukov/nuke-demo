package com.nukedemo.population.batch.layersplittingstep;


import com.nukedemo.GhslMetaData;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import static com.nukedemo.population.batch.layersplittingstep.LayerSplittingStepConfiguration.TARGET_RESOLUTION;
import static com.nukedemo.population.batch.populationstep.PopulationDataWriter.POPULATION_IMG_FOLDER;
import static com.nukedemo.shared.utils.NdJsonUtils.toJson;

@Component
public class LayerSplittingStepCompletionListener implements StepExecutionListener {

    @Value("${populationBatch.ghsl.basePath}")
    private String basePath;

    private final ConcurrentHashMap<String, GhslMetaData> metaData;

    public LayerSplittingStepCompletionListener() {
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
