package com.nukedemo.population.batch.layersplittingstep;


import com.nukedemo.GhslMetaData;
import com.nukedemo.population.batch.shared.BatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.nukedemo.population.batch.layersplittingstep.LayerSplittingStepConfiguration.ORIGINAL_RESOLUTION;
import static com.nukedemo.population.batch.populationstep.PopulationDataWriter.POPULATION_IMG_FOLDER;
import static com.nukedemo.population.batch.shared.BatchUtils.getMetaData;

@Slf4j
@StepScope
@Service
public class LayerSplittingDataPartitioner implements Partitioner {

    @Value("${populationBatch.ghsl.basePath}")
    private String basePath;

    @Value("${populationBatch.ghsl.maxRow}")
    private int maxRow;

    @Value("${populationBatch.ghsl.maxColumn}")
    private int maxColumn;

    public LayerSplittingDataPartitioner() {
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<LayerSplittingInputItem> items = getItems();
        return BatchUtils.executions(gridSize, items);
    }

    private List<LayerSplittingInputItem> getItems() {
        List<LayerSplittingInputItem> items = null;
        try {
            items = getInputItems();
        } catch (IOException e) {
            throw new RuntimeException("Partitioning failed", e);
        }
        return items;
    }

    private List<LayerSplittingInputItem> getInputItems() throws IOException {
        Map<String, GhslMetaData> meta = getMetaData(basePath + "/" + ORIGINAL_RESOLUTION + "/");
        List<LayerSplittingInputItem> items = new ArrayList<>();
        for (String key : meta.keySet()) {
            File file = getFile(key + ".png");
            if (file.exists() && file.isFile()) {
                items.add(new LayerSplittingInputItem(key, meta.get(key), file));
            }
        }
        return items;
    }

    private File getFile(String fileName) {
        return new File(basePath + "/" + ORIGINAL_RESOLUTION + POPULATION_IMG_FOLDER, fileName);
    }

}
