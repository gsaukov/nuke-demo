package com.nukedemo.population.batch.ghslstep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${populationBatch.ghsl.resolution}")
    String resolution;

    @Value("${populationBatch.ghsl.resultFolder}")
    String resultFolder;

    public GhslFileDataWriter() throws IOException {
        Files.createDirectories(Paths.get(resultFolder));
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
        String path = resultFolder + "R" + inputItem.getRow() + "_C" + inputItem.getColumn() + ".zip";
        File outputFile = new File(path);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(item.getGhslData());
            log.info("Written: " + path);
        } catch (Exception e) {
            log.error("Failed to write: " + path, e);
        }
    }

}
