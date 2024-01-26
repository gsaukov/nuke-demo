package com.nukedemo.population.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@StepScope
public class PopulationDataWriter implements ItemWriter<PopulationDataItem> {

    private static final String POPULATION_FOLDER = "./data/res/population/";

    public PopulationDataWriter() throws IOException {
        Files.createDirectories(Paths.get(POPULATION_FOLDER));
    }

    @Override
    public void write(Chunk<? extends PopulationDataItem> chunk) {
        for(PopulationDataItem item : chunk.getItems()){
            writeToFile(POPULATION_FOLDER + item.getAreaCode(), item.getPopulationData());
        }
    }

    private void writeToFile(String fileName, String data) {
        try (FileWriter writer = new FileWriter(fileName);) {
            writer.write(data);
        }
        catch (Exception e) {
            log.error("Failed to create file for: " + fileName, e);
        }
    }

}
