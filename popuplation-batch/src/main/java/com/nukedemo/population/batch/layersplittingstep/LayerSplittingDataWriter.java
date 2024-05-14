package com.nukedemo.population.batch.layersplittingstep;

import com.nukedemo.population.batch.layercompressionstep.LayerCompressionInputItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.nukedemo.population.batch.layersplittingstep.LayerSplittingStepConfiguration.TARGET_RESOLUTION;
import static com.nukedemo.population.batch.populationstep.PopulationDataWriter.POPULATION_IMG_FOLDER;

@Slf4j
@Service
@StepScope
public class LayerSplittingDataWriter implements ItemWriter<LayerSplittingDataItem> {

    @Value("${populationBatch.ghsl.basePath}")
    private String basePath;

    public LayerSplittingDataWriter() {
    }

    @Override
    public void write(Chunk<? extends LayerSplittingDataItem> chunk) {
        for(LayerSplittingDataItem item : chunk.getItems()){
            try {
                writeToPngFile(item);
            } catch (Exception e) {
                throw new RuntimeException("Writing item failed: " + item.getInputItem().getKey(), e);
            }
        }
    }

    private void writeToPngFile(LayerSplittingDataItem item) throws IOException {
        File outputFolder = new File(basePath + "/" + TARGET_RESOLUTION + POPULATION_IMG_FOLDER);
        for(Map.Entry<String, byte[]> entry : item.getSplitImages().entrySet()) {
            FileUtils.writeByteArrayToFile(new File(outputFolder, entry.getKey() + ".png"), entry.getValue());
        }
    }

}
