package com.nukedemo.population.batch.populationstep;

import com.nukedemo.GhslMetaData;
import com.nukedemo.shared.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@StepScope
public class PopulationDataWriter implements ItemWriter<PopulationDataItem> {

    public static String POPULATION_JSON_FOLDER = "populationJson";

    private final String resultFolder;
    private final File outputFolder;

    public PopulationDataWriter(@Value("${populationBatch.ghsl.resultFolder}") String resultFolder) throws IOException {
        Files.createDirectories(Paths.get(resultFolder + "/" + POPULATION_JSON_FOLDER));
        this.resultFolder = resultFolder;
        this.outputFolder = new File(resultFolder + "/" + POPULATION_JSON_FOLDER + "/");
    }

    @Override
    public void write(Chunk<? extends PopulationDataItem> chunk) {
        for(PopulationDataItem item : chunk.getItems()){
            try {
                writeToTifFile(item);
                writeToPopulationDataIntFile(item);
                writeToPopulationDataDoubleFile(item);
            } catch (Exception e) {
                throw new RuntimeException("Writing item failed: " + item.getDataName(), e);
            }
        }
    }

    private void writeToTifFile(PopulationDataItem item) throws IOException {
        File outputFolder = new File(resultFolder + "/temp/");
        FileUtils.writeByteArrayToFile(new File(outputFolder, item.getDataName()), item.getTifSource());
    }

    private void writeToPopulationDataIntFile(PopulationDataItem item) throws Exception {
        String itemName = item.getDataName().replace(".tif", "_int.json");
        FileUtils.writeStringToFile(new File(outputFolder, itemName), toJson(item.getMetaData(), item.getPopulationDataInt()));
    }

    private void writeToPopulationDataDoubleFile(PopulationDataItem item) throws Exception {
        String itemName = item.getDataName().replace(".tif", "_double.json");
        FileUtils.writeStringToFile(new File(outputFolder, itemName), toJson(item.getMetaData(), item.getPopulationDataDouble()));
    }

    private String toJson (GhslMetaData metaData, Object data) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("metaData",  metaData);
        objectMap.put("data",  data);
        return NdJsonUtils.toJson(objectMap);
    }

}
