package com.nukedemo.population.batch.ghslstep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@StepScope
public class GhslFileDataWriter implements ItemWriter<GhslFileDataItem> {

    public static final String POPULATION_FOLDER = "./data/res/population/ghsl/";

    public GhslFileDataWriter() throws IOException {
        Files.createDirectories(Paths.get(POPULATION_FOLDER));
    }

    @Override
    public void write(Chunk<? extends GhslFileDataItem> chunk) {
        for(GhslFileDataItem item : chunk.getItems()){
            if(item.getGhslData().length > 0) { //Error download fix manually
                writeFile(item);
            }
        }
    }

    private void writeFile(GhslFileDataItem item) {
        GhslFileInputItem inputItem = item.getInputItem();
        String path = POPULATION_FOLDER + inputItem.getResolution() + "/R" + inputItem.getRow() + "_C" + inputItem.getColumn() + ".zip";
        File outputFile = new File(path);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(item.getGhslData());
            log.info("Written: " + path);
        } catch (Exception e) {
            log.error("Failed to write: " + path, e);
        }
    }

}
