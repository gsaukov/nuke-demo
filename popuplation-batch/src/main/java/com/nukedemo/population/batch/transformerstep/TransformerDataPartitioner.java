package com.nukedemo.population.batch.transformerstep;

import com.nukedemo.population.batch.shared.BatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nukedemo.population.batch.populationstep.PopulationDataWriter.POPULATION_JSON_FOLDER;

@Slf4j
@StepScope
@Service
public class TransformerDataPartitioner implements Partitioner {

    private String resolution;
    private String resultFolder;

    public TransformerDataPartitioner(@Value("${populationBatch.ghsl.resolution}") String resolution, @Value("${populationBatch.ghsl.resultFolder}")String resultFolder) {
        this.resolution = resolution;
        this.resultFolder = resultFolder;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<TransformerInputItem> items = getItems();
        return BatchUtils.executions(gridSize, items);
    }

    private List<TransformerInputItem> getItems(){
        List<TransformerInputItem> items = null;
        try {
            items = getInputItems();
        } catch (IOException e) {
            throw new RuntimeException("Partitioning failed", e);
        }
        return items;
    }

    private List<TransformerInputItem> getInputItems() throws IOException {
        String folderPath = resultFolder + POPULATION_JSON_FOLDER;
        String filePattern = folderPath + "/*" + resolution + "*_int.json";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("file:" + filePattern);
        List<TransformerInputItem> transformerInputItems = new ArrayList<>();
        for (Resource resource : resources) {
            File file = resource.getFile();
            if (file.exists() && file.isFile()) {
                transformerInputItems.add(new TransformerInputItem(file));
            }
        }
        return transformerInputItems;
    }

}
