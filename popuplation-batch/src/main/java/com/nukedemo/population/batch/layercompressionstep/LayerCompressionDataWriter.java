package com.nukedemo.population.batch.layercompressionstep;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionStepConfiguration.SOURCE_PATH;

@Slf4j
@Service
@StepScope
public class LayerCompressionDataWriter implements ItemWriter<LayerCompressionDataItem> {

    public LayerCompressionDataWriter() {
    }

    @Override
    public void write(Chunk<? extends LayerCompressionDataItem> chunk) {
        for(LayerCompressionDataItem item : chunk.getItems()){
            try {
                writeToPngFile(item);
            } catch (Exception e) {
                throw new RuntimeException("Writing item failed: " + item.getMetaData(), e);
            }
        }
    }

    private void writeToPngFile(LayerCompressionDataItem item) throws IOException {
        if(item.getCompressedLayer() != null) {
            File outputFolder = new File(SOURCE_PATH + "/4326_90ss/img/");
            LayerCompressionInputItem inputItem = item.getInputItem();
            String itemName = "R"+ inputItem.getRow() + "_C" + inputItem.getColumn() + ".png";
            FileUtils.writeByteArrayToFile(new File(outputFolder, itemName), item.getCompressedLayer());
        }
    }
}
