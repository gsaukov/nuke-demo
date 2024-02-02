package com.nukedemo.population.batch.ghslstep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@StepScope
public class GhslFileDataWriter implements ItemWriter<GhslFileDataItem> {

    private static final String POPULATION_FOLDER = "./data/res/population/ghsl";

    public GhslFileDataWriter() throws IOException {
        Files.createDirectories(Paths.get(POPULATION_FOLDER));
    }

    @Override
    public void write(Chunk<? extends GhslFileDataItem> chunk) {
        for(GhslFileDataItem item : chunk.getItems()){
            writeFile(POPULATION_FOLDER, item.getGhslFile());
        }
    }

    private void writeFile(String folderPath, File file) {
        try (FileWriter writer = new FileWriter(folderPath);) {

        }
        catch (Exception e) {
            log.error("Failed to create file for: " + folderPath + '/' + file.getName(), e);
        }
    }

}
